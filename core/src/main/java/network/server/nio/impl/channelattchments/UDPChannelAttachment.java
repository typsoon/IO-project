package network.server.nio.impl.channelattchments;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
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
import network.messages.MessagesConfig;
import network.messages.Message.UDPMessage;
import network.messages.utils.ByteBufferDataConsumer;
import network.messages.utils.DataConsumer;
import network.server.AuthenticationService;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;
import network.socketwrappers.SocketTypes.SocketSender;
import network.utils.AccumulatorAdapters;
import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.WhatWasRead;
import network.utils.impl.OrdinaryBytesAccumulator;

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

    private final int sendingThreeshold = sendingByteBuffer.capacity() / 2;

    private final void sendbufferContents(final SocketAddress socketAddress) throws IOException {
        sendingByteBuffer.flip();
        datagramChannel.send(sendingByteBuffer, socketAddress);
        sendingByteBuffer.clear();
    }

    @Override
    public void dispatchMessages() throws IOException {
        // TODO: do this in a nonblocking way
        synchronized (selectionKey) {
            sendingByteBuffer.clear();
            final var iter = pendingMessages.iterator();

            while (iter.hasNext()) {
                final var nextVal = iter.next();

                // TODO: maybe send more messages in a single Datagram
                final var msgQueue = nextVal.messageQueue;
                while (!msgQueue.isEmpty()) {
                    final var msg = msgQueue.poll();

                    msg.encodeAndWrite(sendingBufferDataConsumer);
                    if (sendingByteBuffer.position() >= sendingThreeshold) {
                        sendbufferContents(nextVal.socketAdress);
                    }

                    logger.finer("Message %s sent to user %s".formatted(msg.getSendable(),
                            nextVal.socketAdress));
                }

                // If there are still some messages waiting to be sent
                if (sendingByteBuffer.position() > 0) {
                    sendbufferContents(nextVal.socketAdress);
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
