package network.server.nio.impl;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.ByteBufferDataProducer;
import network.messages.utils.ByteChannelDataReceiver;
import network.messages.utils.DataConsumer;
import network.server.AuthenticationService;
import network.server.nio.BytesAccumulator;
import network.server.nio.BytesAccumulator.ReadData;
import network.server.nio.BytesAccumulator.WhatWasRead;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;
import network.socketwrappers.SocketTypes.SocketSender;

public interface ChannelAttachment<T extends SessionContract> {
    void dispatchMessages() throws IOException;

    List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException;
}

class ChannelAttachmentLoggingUtils {
    static void logNewSessionWillBeCreated(Logger logger, int tokenVal, UserId userId) {
        logger.finer(
                "Received token %d, it is correct and session will be created for the user %d".formatted(
                        tokenVal,
                        userId.id()));
    }

    static void logInvalidToken(Logger logger, int tokenVal) {

    }
}

abstract class ChannelAttachmentTraits<T extends SessionContract> implements ChannelAttachment<T> {
    private final Logger logger = Logger.getGlobal();

    // TODO: inject this
    protected final MessageDecoder messageDecoder = new ConcreteMessageDecoder();

    final void disconnectUser(UserId userId) {
        throw new IllegalStateException("Message from unauthorized user");
    }

    final Collection<Message> readAllDataFromUDPorTCPAccumulator(BytesAccumulator bytesAccumulator,
            ReadableByteChannel clientSocketChannel,
            final Optional<ReadData> readRes, AuthenticationService authenticationService) throws IOException {

        Collection<Message> answer = new LinkedList<>();
        var tempReadRes = readRes.orElse(null);

        while (tempReadRes != null) {
            var msgByteBuf = tempReadRes.byteBuf();

            switch (tempReadRes.whatWasRead()) {
                case TOKEN -> {
                    final var tokenVal = msgByteBuf.getInt();
                    final var userId = authenticationService.getUser(tokenVal);

                    if (userId == null) {
                        logger.severe(bytesAccumulator.getClass().getCanonicalName());
                        ChannelAttachmentLoggingUtils.logInvalidToken(logger, tokenVal);

                        disconnectUser(userId);
                    }

                    tempReadRes = bytesAccumulator.accumulateBytes(clientSocketChannel).orElse(null);
                }

                case MESSAGE -> {
                    final var msg = messageDecoder.decodeMessage(new ByteBufferDataProducer(msgByteBuf));
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

    public ConnectionBasedChannelAttachment(SelectionKey key) {
        this.consumer = new ByteChannelDataReceiver((WritableByteChannel) key.channel());
        this.key = key;
    }

    @Override
    public final void dispatchMessages() throws IOException {
        synchronized (key) {
            for (final Message msg : msgQueue) {
                msg.encodeAndWrite(consumer);

                Logger.getGlobal()
                        .info(() -> "Sending message %s with payload %s".formatted(msg.getClass().getSimpleName(),
                                msg.getSendable()));
            }
            msgQueue.clear();

            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    public final SocketSender<U> getSender() {
        return message -> {
            synchronized (key) {
                msgQueue.add(message);
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                // return Optional.empty();
            }
        };
    }
}

enum State {
    BEFORE_VALIDATION,
    AFTER_VALIDATION
}

class TCPChannelAttachment<T extends SessionContract> extends ConnectionBasedChannelAttachment<T, TCPMessage> {
    private final BytesAccumulator bytesAccumulator = new OrdinaryBytesAccumulator();
    private final ReadableByteChannel clientSocketChannel;
    private State state = State.BEFORE_VALIDATION;

    private final AuthenticationService authenticationService;
    private final SessionCreator<T> sessionCreator;
    private T sessionContract;
    private final MessageDecoder messageDecoder;

    private final Logger logger = Logger.getGlobal();

    public TCPChannelAttachment(SelectionKey key, AuthenticationService authenticationService,
            SessionCreator<T> sessionCreator, MessageDecoder messageDecoder) {
        super(key);
        this.clientSocketChannel = (ReadableByteChannel) key.channel();
        this.sessionCreator = sessionCreator;
        this.authenticationService = authenticationService;
        this.messageDecoder = messageDecoder;
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        final var readRes = bytesAccumulator.accumulateBytes(clientSocketChannel);
        final List<ClientAndTheirMessage<T>> answer = new LinkedList<>();

        if (readRes.isEmpty()) {
            return answer;
        }

        switch (state) {
            case BEFORE_VALIDATION -> {
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
                answer.addAll(readAllDataFromUDPorTCPAccumulator(bytesAccumulator, clientSocketChannel, readRes,
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
    private final BytesAccumulator bytesAccumulator = new SSLSocketBytesAccumulator();
    private final ReadableByteChannel clientSocketChannel;
    private final T clientSession;

    public SSLChannelAttachment(SelectionKey key, T clientSession) {
        super(key);
        this.clientSocketChannel = (ReadableByteChannel) key.channel();
        this.clientSession = clientSession;
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        final var readRes = bytesAccumulator.accumulateBytes(clientSocketChannel);
        final List<ClientAndTheirMessage<T>> answer = new LinkedList<>();

        if (readRes.isEmpty()) {
            return answer;
        }

        var data = readRes.get();
        assert data.whatWasRead() == WhatWasRead.MESSAGE;

        final var msg = messageDecoder.decodeMessage(new ByteBufferDataProducer(data.byteBuf()));
        answer.add(new ClientAndTheirMessage<T>(clientSession, msg));

        return answer;
    }

}

class UDPChannelAttachment<T extends SessionContract> extends ChannelAttachmentTraits<T> {
    @Override
    public void dispatchMessages() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispatchMessages'");
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readIncomingMessages'");
    }

}
