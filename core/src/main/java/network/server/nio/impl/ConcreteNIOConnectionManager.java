package network.server.nio.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Duration;
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
import java.util.WeakHashMap;
import java.util.logging.Logger;

import database.DatabaseManager;
import database.DatabaseManager.UserId;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.utils.ByteBufferDataProducer;
import network.messages.utils.ByteChannelDataReceiver;
import network.server.AuthenticationService;
import network.server.AuthenticationService.Token;
import network.server.nio.BytesAccumulator;
import network.server.nio.NIOConnectionManager;
import network.server.nio.NIOConnectionManager.SessionConcract;
import network.server.nio.NIOSSLSocketServer;
import network.server.nio.NIOSocketServer;
import network.server.nio.BytesAccumulator.WhatWasRead;
import network.socketwrappers.SocketTypes.SocketSender;

public class ConcreteNIOConnectionManager<T extends SessionConcract> implements NIOConnectionManager<T> {

    private static record ChannelAttachment<T, U extends Message>(T clientSession, Queue<U> messageQueue,
            BytesAccumulator bytesAccumulator) {
    }

    private static class QueueInserterWhichReturnsEmptyOptional<T extends Message> implements SocketSender<T> {
        private final Queue<T> queue;
        private final SelectionKey key;

        public QueueInserterWhichReturnsEmptyOptional(Queue<T> queue, SelectionKey key) {
            this.queue = queue;
            this.key = key;
        }

        @Override
        public <U extends Message> Optional<U> sendMessage(T message) throws IOException {
            synchronized (key) {
                queue.add(message);
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                return Optional.empty();
            }
        }
    }

    private final Selector selector;
    private final SessionCreator<T> sessionCreator;
    private final Logger logger = Logger.getGlobal();
    private final MessageDecoder messageDecoder;

    private final NIOSocketServer udpMessageSender;
    private final NIOSocketServer tcpMessageSender;
    private final NIOSSLSocketServer sslMessageSender;

    private final Map<SocketChannel, SSLSocketBytesAccumulator> unauthorizedChannels = new HashMap<>();

    private final DatabaseManager databaseManager;
    private final AuthenticationService authenticationService;

    public ConcreteNIOConnectionManager(DatabaseManager databaseManager,
            AuthenticationService authenticationService, NIOSocketServer udpServer,
            NIOSocketServer tcpServer,
            NIOSSLSocketServer sslServer, SessionCreator<T> sessionCreator) throws IOException {
        this.selector = Selector.open();
        this.messageDecoder = new ConcreteMessageDecoder();
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
    public Collection<ClientAndTheirMessage<T>> select() throws IOException {
        // TODO: this is not optimal -change this
        List<ClientAndTheirMessage<T>> answer = new ArrayList<>();
        selector.select();

        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();

        while (iter.hasNext()) {
            SelectionKey key = iter.next();

            try {
                if (!key.channel().isOpen()) {
                    key.cancel();
                }

                if (key.isAcceptable()) {
                    var serverSocketChannel = (ServerSocketChannel) key.channel();
                    var clientSocketChannel = serverSocketChannel.accept();

                    // User was not authorized yet
                    if (Objects.equals(serverSocketChannel, sslMessageSender.getServerSocketChannel())) {
                        unauthorizedChannels.put(clientSocketChannel, new SSLSocketBytesAccumulator());

                        sslMessageSender.acceptClient(clientSocketChannel);
                    }

                    clientSocketChannel.configureBlocking(false);
                    clientSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                    logger.info(String.format("Client connected %s", clientSocketChannel.getRemoteAddress()));
                }

                if (key.isWritable() && key.attachment() != null) {
                    synchronized (key) {
                        var msgQueue = ((ChannelAttachment<T, ?>) key.attachment()).messageQueue;

                        var consumer = new ByteChannelDataReceiver((SocketChannel) key.channel());
                        for (Message msg : msgQueue) {
                            msg.encodeAndWrite(consumer);
                        }
                        msgQueue.clear();

                        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                        logger.warning("Sending message");
                    }
                }

                // TODO: think about refactoring these continues out
                // logger.info("I am here 1");
                if (key.isReadable()) {
                    // logger.info("I am here 2");
                    var clientSocketChannel = (SocketChannel) key.channel();

                    // if user was not authorized yet
                    if (unauthorizedChannels.keySet().contains(clientSocketChannel)) {
                        // logger.info("I am here 3");
                        // FIXME: think this through - this was a bad idea
                        // ByteBuffer decodedData =
                        // sslMessageSender.preprocessData(clientSocketChannel);

                        // FIXME: this is extremely ugly - change this
                        // if (decodedData == null) {
                        // continue;
                        // }

                        var bytesAccumulator = unauthorizedChannels.get(clientSocketChannel);
                        var msgByteBuffer = bytesAccumulator.accumulateBytes(clientSocketChannel);

                        if (msgByteBuffer.isEmpty()) {
                            continue;
                        }
                        assert msgByteBuffer.get().whatWasRead() == WhatWasRead.MESSAGE;

                        var receivedMessage = messageDecoder
                                .decodeMessage(new ByteBufferDataProducer(msgByteBuffer.get().byteBuf()));
                        logger.info("Received message: %s %s".formatted(receivedMessage, receivedMessage.getAction()));

                        if (receivedMessage instanceof LogInQuery logInQuery) {
                            var id = databaseManager.getUserId(logInQuery.username());

                            Token token = authenticationService.tryAuth(id, logInQuery.password());

                            var session = sessionCreator.getSession(id);
                            var attachment = new ChannelAttachment<T, EncryptedMessage>(session,
                                    new ArrayDeque<>(), bytesAccumulator);
                            var sslSender = new QueueInserterWhichReturnsEmptyOptional<EncryptedMessage>(
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

                        continue;
                    }

                    // If nothing was attached to this channel yet
                    if (key.attachment() == null) {
                        key.attach(new SSLSocketBytesAccumulator());
                    }

                    if (key.attachment() instanceof BytesAccumulator bytesAcc) {
                        var tokenRes = bytesAcc.accumulateBytes(clientSocketChannel);

                        if (tokenRes.isEmpty()) {
                            continue;
                        }
                        assert tokenRes.get().whatWasRead() == WhatWasRead.TOKEN;

                        var serverSocketChannel = (ServerSocketChannel) key.channel();
                        var userIdVal = tokenRes.get().byteBuf().getInt();
                        UserId userId = authenticationService.getUser(userIdVal);
                        if (userId == null) {
                            logger.warning(
                                    String.format("Received message from unauthorized user - invalid token %s",
                                            clientSocketChannel.getRemoteAddress()));
                            continue;
                        }

                        var session = sessionCreator.getSession(userId);

                        if (Objects.equals(udpMessageSender.getServerSocketChannel(), serverSocketChannel)) {
                            var attachment = new ChannelAttachment<T, UDPMessage>(session, new ArrayDeque<>(),
                                    new OrdinaryBytesAccumulator());
                            key.attach(attachment);

                            var udpSender = new QueueInserterWhichReturnsEmptyOptional<>(attachment.messageQueue, key);
                            session.getMessageDispatcher().connectUDPSender(udpSender);
                        } else if (Objects.equals(tcpMessageSender.getServerSocketChannel(), serverSocketChannel)) {
                            var attachment = new ChannelAttachment<T, TCPMessage>(session, new ArrayDeque<>(),
                                    new OrdinaryBytesAccumulator());
                            key.attach(attachment);

                            var tcpSender = new QueueInserterWhichReturnsEmptyOptional<>(attachment.messageQueue, key);
                            session.getMessageDispatcher().connectTCPSender(tcpSender);
                        }
                        continue;
                    }

                    var attachment = (ChannelAttachment<T, ?>) key.attachment();
                    var msgByteBuffer = attachment.bytesAccumulator.accumulateBytes(clientSocketChannel);

                    if (msgByteBuffer.isEmpty()) {
                        continue;
                    }

                    switch (msgByteBuffer.get().whatWasRead()) {
                        case TOKEN -> {
                            var tokenVal = msgByteBuffer.get().byteBuf().getInt();
                            var userId = authenticationService.getUser(tokenVal);

                            if (userId == null) {
                                logger.warning(
                                        String.format("Received message from unauthorized user - invalid token %s",
                                                clientSocketChannel.getRemoteAddress()));

                                // TODO: disconnect user
                                throw new IllegalStateException("Message from inauthorized user");
                            }
                        }

                        case MESSAGE -> {
                            var msg = messageDecoder
                                    .decodeMessage(new ByteBufferDataProducer(msgByteBuffer.get().byteBuf()));
                            answer.add(new ClientAndTheirMessage<T>(attachment.clientSession, msg));
                        }
                    }
                }

            } catch (Exception e) {
                logger.severe(String.format("An error occured at server loop %s", e));
                throw e;
            } finally {
                iter.remove();
            }
        }

        return answer;
    }

    public void registerSocketServer(NIOSocketServer nioSocketServer) throws IOException {
        nioSocketServer.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);
    }

}
