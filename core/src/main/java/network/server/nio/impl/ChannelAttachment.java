package network.server.nio.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.MessagesConfig;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.ByteBufferDataConsumer;
import network.messages.utils.ByteBufferDataProducer;
import network.messages.utils.ByteChannelDataReceiver;
import network.messages.utils.DataConsumer;
import network.server.AuthenticationService;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;
import network.socketwrappers.SocketTypes.SocketSender;
import network.utils.AccumulatorAdapters;
import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.Readable;
import network.utils.BytesAccumulator.WhatWasRead;
import network.utils.impl.OnlyMessagesBytesAccumulator;
import network.utils.impl.OrdinaryBytesAccumulator;

public interface ChannelAttachment<T extends SessionContract> {
    void dispatchMessages() throws IOException;

    List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException;
}

class ChannelAttachmentLoggingUtils {
    static void logNewSessionWillBeCreated(final Logger logger, final int tokenVal, final UserId userId) {
        logger.finer(
                "Received token %d, it is correct and session will be created for the user %d".formatted(
                        tokenVal,
                        userId.id()));
    }

    static void logInvalidToken(final Logger logger, final int tokenVal) {

    }
}

abstract class ChannelAttachmentTraits<T extends SessionContract> implements ChannelAttachment<T> {
    private final Logger logger = Logger.getGlobal();

    // TODO: inject this
    protected final MessageDecoder messageDecoder = new ConcreteMessageDecoder();

    final void disconnectUser(final UserId userId) {
        throw new IllegalStateException("Message from unauthorized user");
    }

    final Collection<Message> readAllDataFromUDPorTCPAccumulator(final BytesAccumulator bytesAccumulator,
            final BytesAccumulator.Readable readable,
            final AuthenticationService authenticationService) throws IOException {
        final Collection<Message> answer = new LinkedList<>();
        final var readRes = bytesAccumulator.accumulateBytes(readable);
        var tempReadRes = readRes.orElse(null);

        while (tempReadRes != null) {
            final var msgByteBuf = tempReadRes.byteBuf();

            switch (tempReadRes.whatWasRead()) {
                case TOKEN -> {
                    final var tokenVal = msgByteBuf.getInt();
                    final var userId = authenticationService.getUser(tokenVal);

                    if (userId == null) {
                        logger.severe(bytesAccumulator.getClass().getCanonicalName());
                        ChannelAttachmentLoggingUtils.logInvalidToken(logger, tokenVal);

                        disconnectUser(userId);
                    }

                    tempReadRes = bytesAccumulator.accumulateBytes(readable).orElse(null);
                }

                case MESSAGE -> {
                    // DebugUtils.printBuffer(msgByteBuf);

                    final var msg = messageDecoder.decodeMessage(new ByteBufferDataProducer(msgByteBuf));
                    // final var msg = messageDecoder.decodeMessage(new
                    // ByteBufferDataProducer(tempMsgByteBuf));
                    answer.add(msg);
                    tempReadRes = null;
                }
            }
        }

        return answer;
    }
}

abstract class ConnectionBasedChannelAttachment<T extends SessionContract, U extends Message>
        extends ChannelAttachmentTraits<T> {
    protected final Queue<Message> msgQueue = new ConcurrentLinkedQueue<>();
    protected final SelectionKey key;
    private final DataConsumer consumer;

    public ConnectionBasedChannelAttachment(final SelectionKey key) {
        this.consumer = new ByteChannelDataReceiver((WritableByteChannel) key.channel());
        this.key = key;
    }

    @Override
    public final void dispatchMessages() throws IOException {
        synchronized (key) {
            for (final Message msg : msgQueue) {
                // Logger.getGlobal().info("I AM HERE AND SENDING
                // %s".formatted(msg.getSendable()));

                msg.encodeAndWrite(consumer);

                // Logger.getGlobal()
                // .info(() -> "Sending message %s with payload
                // %s".formatted(msg.getClass().getSimpleName(),
                // msg.getSendable()));
            }
            msgQueue.clear();

            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    public final SocketSender<U> getSender() {
        return message -> {
            synchronized (key) {
                Logger.getGlobal().finest("Message %s enqueued".formatted(message.getSendable()));

                msgQueue.add(message);
                key.interestOpsOr(SelectionKey.OP_WRITE);

            }

            key.selector().wakeup();
        };
    }
}

enum State {
    BEFORE_VALIDATION,
    AFTER_VALIDATION
}

class TCPChannelAttachment<T extends SessionContract> extends ConnectionBasedChannelAttachment<T, TCPMessage> {
    private final BytesAccumulator bytesAccumulator = new OrdinaryBytesAccumulator();
    private final BytesAccumulator.Readable clientSocketChannel;
    private State state = State.BEFORE_VALIDATION;

    private final AuthenticationService authenticationService;
    private final SessionCreator<T> sessionCreator;
    private T sessionContract;
    @SuppressWarnings("unused")
    private final MessageDecoder messageDecoder;

    private final Logger logger = Logger.getGlobal();

    public TCPChannelAttachment(final SelectionKey key, final AuthenticationService authenticationService,
            final SessionCreator<T> sessionCreator, final MessageDecoder messageDecoder) {
        super(key);
        this.clientSocketChannel = AccumulatorAdapters
                .getRedableByteChannelAdapter((ReadableByteChannel) key.channel());
        this.sessionCreator = sessionCreator;
        this.authenticationService = authenticationService;
        this.messageDecoder = messageDecoder;
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        final List<ClientAndTheirMessage<T>> answer = new LinkedList<>();

        switch (state) {
            case BEFORE_VALIDATION -> {
                final var readRes = bytesAccumulator.accumulateBytes(clientSocketChannel);
                if (readRes.isEmpty()) {
                    return answer;
                }

                assert readRes.get().whatWasRead() == WhatWasRead.TOKEN;

                final var userTokenVal = readRes.get().byteBuf().getInt();
                readRes.get().byteBuf().flip();

                final UserId userId = authenticationService.getUser(userTokenVal);

                // NOTE: auth was not succesfull
                if (userId == null) {
                    ChannelAttachmentLoggingUtils.logInvalidToken(logger, userTokenVal);
                    return answer;
                }

                ChannelAttachmentLoggingUtils.logNewSessionWillBeCreated(logger, userTokenVal, userId);

                sessionContract = sessionCreator.getSession(userId);
                sessionContract.getMessageDispatcher().connectTCPSender(getSender());

                state = State.AFTER_VALIDATION;
            }

            case AFTER_VALIDATION -> {
                answer.addAll(readAllDataFromUDPorTCPAccumulator(bytesAccumulator, clientSocketChannel,
                        authenticationService)
                        .stream()
                        .map(msg -> new ClientAndTheirMessage<T>(sessionContract, msg))
                        .toList());
            }
        }

        return answer;
    }

}

class SSLChannelAttachment<T extends SessionContract> extends ConnectionBasedChannelAttachment<T, EncryptedMessage> {
    // TODO: think about this
    private final BytesAccumulator bytesAccumulator = new OnlyMessagesBytesAccumulator();
    private final Readable clientSocketChannel;
    private final T clientSession;

    public SSLChannelAttachment(final SelectionKey key, final T clientSession) {
        super(key);
        this.clientSocketChannel = AccumulatorAdapters
                .getRedableByteChannelAdapter((ReadableByteChannel) key.channel());
        this.clientSession = clientSession;
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        final var readRes = bytesAccumulator.accumulateBytes(clientSocketChannel);
        final List<ClientAndTheirMessage<T>> answer = new LinkedList<>();

        if (readRes.isEmpty()) {
            return answer;
        }

        final var data = readRes.get();
        assert data.whatWasRead() == WhatWasRead.MESSAGE;

        final var msg = messageDecoder.decodeMessage(new ByteBufferDataProducer(data.byteBuf()));
        answer.add(new ClientAndTheirMessage<T>(clientSession, msg));

        return answer;
    }

}

class UDPChannelAttachment<T extends SessionContract> extends ChannelAttachmentTraits<T> {
    private static record ReceiverAndMessages(SocketAddress socketAdress,
            Queue<Message> messageQueue) {
    }

    private static record UserData<T>(Queue<Message> msgQueue, BytesAccumulator bytesAccumulator,
            T clientSession) {
    }

    private final Logger logger = Logger.getGlobal();
    private final Set<ReceiverAndMessages> pendingMessages = ConcurrentHashMap.newKeySet();

    private final SelectionKey selectionKey;
    private final DatagramChannel datagramChannel;
    private final AuthenticationService authenticationService;
    private final ByteBuffer receivingByteBuffer = ByteBuffer.allocateDirect(MessagesConfig.maxUdpPacketLength);

    private final ByteBuffer sendingByteBuffer = ByteBuffer.allocateDirect(MessagesConfig.maxUdpPacketLength);

    private final SessionCreator<T> sessionCreator;

    private final Map<SocketAddress, UserData<T>> allUsers = new ConcurrentHashMap<>();

    private final Map<SocketAddress, BytesAccumulator> unauthorizedUsers = new ConcurrentHashMap<>();

    // private final AtomicBoolean noPendingResponses = new AtomicBoolean();

    public UDPChannelAttachment(final SelectionKey selectionKey, final DatagramChannel datagramChannel,
            final AuthenticationService authenticationService, final SessionCreator<T> sessionCreator) {
        this.selectionKey = selectionKey;
        this.datagramChannel = datagramChannel;
        this.authenticationService = authenticationService;
        this.sessionCreator = sessionCreator;
    }

    private final SocketSender<UDPMessage> getSender(final SocketAddress socketAddress) {
        final var userData = Objects.requireNonNull(allUsers.get(socketAddress));
        final var queue = userData.msgQueue;

        return message -> {
            queue.add(message);

            synchronized (selectionKey) {
                // logger.info("Message %s enqueued, user %s".formatted(message.getSendable(),
                // socketAddress));
                //
                pendingMessages.add(new ReceiverAndMessages(socketAddress, queue));

                selectionKey.interestOpsOr(SelectionKey.OP_WRITE);
            }

            selectionKey.selector().wakeup();
        };
    }

    private final DataConsumer sendingBufferDataConsumer = new ByteBufferDataConsumer(sendingByteBuffer);

    @Override
    public void dispatchMessages() throws IOException {
        // TODO: do this in a nonblocking way
        synchronized (selectionKey) {
            final var iter = pendingMessages.iterator();

            while (iter.hasNext()) {
                final var nextVal = iter.next();

                // TODO: maybe send more messages in a single Datagram
                for (final Message msg : nextVal.messageQueue) {
                    sendingByteBuffer.clear();

                    msg.encodeAndWrite(sendingBufferDataConsumer);
                    datagramChannel.send(sendingByteBuffer, nextVal.socketAdress);

                    logger.info("Message %s sent to user %s".formatted(msg.getSendable(),
                            nextVal.socketAdress));
                }

                iter.remove();
            }

            // NOTE: no longer interested in writing
            selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        receivingByteBuffer.clear();
        final var address = datagramChannel.receive(receivingByteBuffer);

        receivingByteBuffer.flip();
        // logger.finest("Received %d bytes".formatted(receivingByteBuffer.limit()));

        final List<ClientAndTheirMessage<T>> answer = new LinkedList<>();
        final var byteBufAdapter = AccumulatorAdapters.getByteBufferAdapter(receivingByteBuffer);

        if (!allUsers.containsKey(address)) {
            final BytesAccumulator bytesAccumulator = unauthorizedUsers.computeIfAbsent(address,
                    adress -> new OrdinaryBytesAccumulator());

            final var readRes = bytesAccumulator.accumulateBytes(byteBufAdapter);
            if (readRes.isEmpty()) {
                return answer;
            }

            assert readRes.get().whatWasRead() == WhatWasRead.TOKEN;

            final var userTokenVal = readRes.get().byteBuf().getInt();
            readRes.get().byteBuf().flip();

            final UserId userId = authenticationService.getUser(userTokenVal);

            // NOTE: auth was not succesfull
            if (userId == null) {
                ChannelAttachmentLoggingUtils.logInvalidToken(logger, userTokenVal);
                return answer;
            }

            ChannelAttachmentLoggingUtils.logNewSessionWillBeCreated(logger, userTokenVal, userId);
            authorizeUser(address, userId, bytesAccumulator);
        }

        final var userData = allUsers.get(address);

        // logger.finest("position %d limit
        // %d".formatted(receivingByteBuffer.position(),
        // receivingByteBuffer.limit()));

        answer.addAll(readAllDataFromUDPorTCPAccumulator(userData.bytesAccumulator, byteBufAdapter,
                authenticationService)
                .stream()
                .map(msg -> new ClientAndTheirMessage<T>(userData.clientSession, msg))
                .toList());

        return answer;
    }

    private final UserData<T> authorizeUser(final SocketAddress address, final UserId userId,
            final BytesAccumulator bytesAccumulator) {
        final var sessionContract = sessionCreator.getSession(userId);

        unauthorizedUsers.remove(address);

        final Queue<Message> queue = new ConcurrentLinkedQueue<>();

        final UserData<T> userData = new UserData<T>(queue, bytesAccumulator, sessionContract);
        allUsers.put(address, userData);

        sessionContract.getMessageDispatcher().connectUDPSender(getSender(address));

        return userData;
    }
}
