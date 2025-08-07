package network.server.nio.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

import database.DatabaseManager;
import database.DatabaseManager.UserId;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.loginstate.PortInfoRequest;
import network.messages.loginstate.PortInfoResponse;
import network.messages.utils.ByteBufferDataProducer;
import network.messages.utils.ByteChannelDataReceiver;
import network.server.AuthenticationService;
import network.server.AuthenticationService.Token;
import network.server.nio.BytesAccumulator;
import network.server.nio.BytesAccumulator.ReadData;
import network.server.nio.BytesAccumulator.WhatWasRead;
import network.server.nio.NIOConnectionManager;
import network.server.nio.NIOConnectionManager.SessionConcract;
import network.server.nio.NIOSSLSocketServer;
import network.server.nio.NIOSocketServer;
import network.socketwrappers.SocketTypes.SocketSender;

public class ConcreteNIOConnectionManager<T extends SessionConcract> implements NIOConnectionManager<T> {

    private static record ChannelAttachment<T, U extends Message>(T clientSession, Queue<U> messageQueue,
            BytesAccumulator bytesAccumulator) {
    }

    private static class QueueInserter<T extends Message> implements SocketSender<T> {
        private final Queue<T> queue;
        private final SelectionKey key;

        public QueueInserter(final Queue<T> queue, final SelectionKey key) {
            this.queue = queue;
            this.key = key;
        }

        @Override
        // public <U extends Message> void sendMessage(T message) throws IOException {
        public void sendMessage(final T message) throws IOException {
            synchronized (key) {
                queue.add(message);
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                // return Optional.empty();
            }
        }
    }

    private final Selector selector;
    private final SessionCreator<T> sessionCreator;
    private final Logger logger = Logger.getGlobal();
    private final MessageDecoder messageDecoder;
    private final ObjectToMessageDecoder objectToMessageDecoder;

    private final NIOSocketServer udpMessageSender;
    private final NIOSocketServer tcpMessageSender;
    private final NIOSSLSocketServer sslMessageSender;

    private final Map<SocketChannel, SSLSocketBytesAccumulator> unauthorizedChannels = new HashMap<>();

    private final DatabaseManager databaseManager;
    private final AuthenticationService authenticationService;

    public ConcreteNIOConnectionManager(final DatabaseManager databaseManager,
            final AuthenticationService authenticationService, final NIOSocketServer udpServer,
            final NIOSocketServer tcpServer,
            final NIOSSLSocketServer sslServer, final SessionCreator<T> sessionCreator,
            final ObjectToMessageDecoder objectToMessageDecoder) throws IOException {
        this.selector = Selector.open();
        this.messageDecoder = new ConcreteMessageDecoder();
        this.objectToMessageDecoder = objectToMessageDecoder;
        this.databaseManager = databaseManager;
        this.udpMessageSender = udpServer;
        this.tcpMessageSender = tcpServer;
        this.sslMessageSender = sslServer;
        this.sessionCreator = sessionCreator;
        this.authenticationService = authenticationService;

        registerSocketServer(udpServer);
        registerSocketServer(tcpServer);
        registerSocketServer(sslServer);
    }

    // TODO: there is room for improvement: we could be parsing messages in
    // different
    // thread than the one we use for selecting
    @Override
    public final Collection<ClientAndTheirMessage<T>> select() throws IOException {
        // TODO: this is not optimal -change this
        final List<ClientAndTheirMessage<T>> answer = new ArrayList<>();
        selector.select();

        final Set<SelectionKey> keys = selector.selectedKeys();
        final Iterator<SelectionKey> iter = keys.iterator();

        while (iter.hasNext()) {
            final SelectionKey key = iter.next();

            try {
                // Unregister a key if a channel is no longer open
                if (!key.channel().isOpen()) {
                    key.cancel();
                }

                if (key.isAcceptable()) {
                    handleIncomingConnection(key);
                }

                if (key.isWritable() && key.attachment() != null) {
                    sendMessagesToUser(key);
                }

                // TODO: think about refactoring these continues out
                if (key.isReadable()) {
                    final var clientSocketChannel = (SocketChannel) key.channel();

                    // if user was not authorized yet
                    if (unauthorizedChannels.containsKey(clientSocketChannel)) {
                        handleUnauthorizedUser(clientSocketChannel, key);
                        continue;
                    }

                    // If nothing was attached to this channel yet
                    if (key.attachment() == null) {
                        // TODO: Not sure if it is SSLSocketBytesAccumulator that should be attached
                        // here, think about it and potentially change it
                        key.attach(new OrdinaryBytesAccumulator());
                    }

                    Optional<ReadData> msgByteBuffer;
                    if (key.attachment() instanceof final BytesAccumulator bytesAcc) {
                        msgByteBuffer = readTokenAndSetUpTCPorUDPconnection(bytesAcc, clientSocketChannel, key);

                    } else {
                        // Read incoming bytes
                        final var attachment = (ChannelAttachment<T, ? extends Message>) key.attachment();
                        msgByteBuffer = attachment.bytesAccumulator.accumulateBytes(clientSocketChannel);
                    }

                    if (msgByteBuffer.isEmpty()) {
                        // Waiting for the rest of the bytes
                        continue;
                    }

                    // Handle read bytes
                    final var attachment = (ChannelAttachment<T, ? extends Message>) key.attachment();

                    while (!msgByteBuffer.isEmpty()) {
                        switch (msgByteBuffer.get().whatWasRead()) {
                            case TOKEN -> {
                                final var tokenVal = msgByteBuffer.get().byteBuf().getInt();
                                final var userId = authenticationService.getUser(tokenVal);

                                if (userId == null) {
                                    logger.severe(attachment.bytesAccumulator.getClass().getCanonicalName());

                                    logger.warning(
                                            String.format(
                                                    "Received message from unauthorized user %s - invalid token %d",
                                                    clientSocketChannel.getRemoteAddress(), tokenVal));

                                    // TODO: disconnect user
                                    throw new IllegalStateException("Message from unauthorized user");
                                }

                                msgByteBuffer = attachment.bytesAccumulator.accumulateBytes(clientSocketChannel);
                            }

                            case MESSAGE -> {
                                handleMessageBytes(msgByteBuffer.get(), answer, attachment, key);
                                msgByteBuffer = Optional.empty();
                            }
                        }
                    }
                }

            } catch (final Exception e) {
                logger.severe(String.format("An error occured at server loop %s", e));
                throw e;
            } finally {
                iter.remove();
            }
        }

        return answer;
    }

    private final void handleUnauthorizedUser(final SocketChannel clientSocketChannel, final SelectionKey key)
            throws IOException {
        // logger.info("I am here 3");
        // FIXME: think this through - this was a bad idea
        // ByteBuffer decodedData =
        // sslMessageSender.preprocessData(clientSocketChannel);

        // FIXME: this is extremely ugly - change this
        // if (decodedData == null) {
        // continue;
        // }

        final var bytesAccumulator = unauthorizedChannels.get(clientSocketChannel);
        final var msgByteBuffer = bytesAccumulator.accumulateBytes(clientSocketChannel);

        if (msgByteBuffer.isEmpty()) {
            return;
        }

        assert msgByteBuffer.get().whatWasRead() == WhatWasRead.MESSAGE;

        final var receivedMessage = messageDecoder
                .decodeMessage(new ByteBufferDataProducer(msgByteBuffer.get().byteBuf()));
        logger.info(
                "Received message: %s %s".formatted(receivedMessage, receivedMessage.getSendable()));

        if (receivedMessage instanceof final LogInQuery logInQuery) {
            final var id = databaseManager.getUserId(logInQuery.username());

            final Token token = authenticationService.tryAuth(id, logInQuery.password());

            final var session = sessionCreator.getSession(id);
            final var attachment = new ChannelAttachment<T, EncryptedMessage>(session,
                    new ArrayDeque<>(), bytesAccumulator);
            final var sslSender = new QueueInserter<EncryptedMessage>(
                    attachment.messageQueue(), key);
            key.attach(attachment);

            if (token != null) {
                session.getMessageDispatcher()
                        .connectSSLSender(sslSender);
                sslSender.sendMessage(new LogInResponse(Optional.of(token.val())));

                unauthorizedChannels.remove(clientSocketChannel);

                logger.info("Successfully authenticated user: %s login: %s".formatted(id.id(),
                        logInQuery.username()));
            } else {
                sslSender.sendMessage(new LogInResponse(Optional.empty()));
                logger.info("Auth for user id: %s login: %s was unsuccessfull".formatted(id,
                        logInQuery.username()));
            }
        } else {
            logger.severe(
                    "Received unexpected message %s from unauthorized user".formatted(receivedMessage));
        }
    }

    private final void sendMessagesToUser(final SelectionKey key) throws IOException {
        synchronized (key) {
            final var msgQueue = ((ChannelAttachment<T, ?>) key.attachment()).messageQueue;

            final var consumer = new ByteChannelDataReceiver((SocketChannel) key.channel());
            for (final Message msg : msgQueue) {
                msg.encodeAndWrite(consumer);
                logger.info(() -> "Sending message %s with payload %s".formatted(msg.getClass().getSimpleName(),
                        msg.getSendable()));
            }
            msgQueue.clear();

            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    private final void handleIncomingConnection(final SelectionKey key) throws IOException {
        final var serverSocketChannel = (ServerSocketChannel) key.channel();
        final var clientSocketChannel = serverSocketChannel.accept();

        // New user connected to SSLServer - mark them as unauthorized
        if (Objects.equals(serverSocketChannel, sslMessageSender.getServerSocketChannel())) {
            unauthorizedChannels.put(clientSocketChannel, new SSLSocketBytesAccumulator());

            sslMessageSender.acceptClient(clientSocketChannel);
        }

        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        logger.info(String.format("Client connected %s", clientSocketChannel.getRemoteAddress()));
    }

    // Returns true if the connection was succesfully set up
    private final Optional<ReadData> readTokenAndSetUpTCPorUDPconnection(final BytesAccumulator bytesAcc,
            final SocketChannel clientSocketChannel, final SelectionKey key) throws IOException {
        final var tokenRes = bytesAcc.accumulateBytes(clientSocketChannel);

        if (tokenRes.isEmpty()) {
            return Optional.empty();
        }
        assert tokenRes.get().whatWasRead() == WhatWasRead.TOKEN;

        final var userTokenVal = tokenRes.get().byteBuf().getInt();
        tokenRes.get().byteBuf().flip();

        final UserId userId = authenticationService.getUser(userTokenVal);
        if (userId == null) {
            logger.warning(
                    String.format("Received message from unauthorized user - invalid token %s",
                            clientSocketChannel.getRemoteAddress()));
            return Optional.empty();
        }

        logger.finer(
                "Received token %d, it is correct and session will be created for the user %d".formatted(userTokenVal,
                        userId.id()));

        final var session = sessionCreator.getSession(userId);

        final var channel = ((SocketChannel) key.channel()).getLocalAddress();
        var port = ((InetSocketAddress) channel).getPort();

        if (udpMessageSender.getPort() == port) {
            final var attachment = new ChannelAttachment<T, UDPMessage>(session, new ArrayDeque<>(),
                    bytesAcc);
            key.attach(attachment);

            final var udpSender = new QueueInserter<>(attachment.messageQueue, key);
            session.getMessageDispatcher().connectUDPSender(udpSender);

            return tokenRes;
        } else if (tcpMessageSender.getPort() == port) {
            final var attachment = new ChannelAttachment<T, TCPMessage>(session, new ArrayDeque<>(),
                    bytesAcc);

            key.attach(attachment);

            final var tcpSender = new QueueInserter<>(attachment.messageQueue, key);
            session.getMessageDispatcher().connectTCPSender(tcpSender);

            return tokenRes;
        } else {
            throw new IllegalStateException("Port doesn't correspond to a tcp or udp socket");
        }
    }

    private final <U extends Message> void handleMessageBytes(final BytesAccumulator.ReadData msgByteBuffer,
            final List<ClientAndTheirMessage<T>> answer,
            final ChannelAttachment<T, U> attachment, final SelectionKey key) throws IOException {
        final var msg = messageDecoder
                .decodeMessage(new ByteBufferDataProducer(msgByteBuffer.byteBuf()));

        if (msg instanceof Message.EncryptedMessage encryptedMsg) {
            final var realType = (ChannelAttachment<T, Message.EncryptedMessage>) attachment;

            switch (encryptedMsg) {
                case final PortInfoRequest portInfoReq -> {
                    final var sslSender = new QueueInserter<Message.EncryptedMessage>(
                            realType.messageQueue(), key);

                    // FIXME: fix ugly casts
                    final var portInfoResponse = (Message.EncryptedMessage) objectToMessageDecoder
                            .decodeFromRecord(new PortInfoResponse.Payload(udpMessageSender.getPort(),
                                    tcpMessageSender.getPort()));
                    sslSender.sendMessage(portInfoResponse);
                }

                default -> {
                }
            }

        } else {
            answer.add(new ClientAndTheirMessage<T>(attachment.clientSession, msg));
        }
    }

    public void registerSocketServer(final NIOSocketServer nioSocketServer) throws IOException {
        nioSocketServer.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);
    }

}
