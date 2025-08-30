package network.server.nio.impl;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import database.IDatabaseManager;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.loginstate.PortInfoRequest;
import network.messages.loginstate.PortInfoResponse;
import network.messages.utils.ByteBufferDataProducer;
import network.server.AuthenticationService;
import network.server.AuthenticationService.Token;
import network.server.nio.BytesAccumulator.WhatWasRead;
import network.server.nio.NIOConnectionManager;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOSSLSocketServer;
import network.server.nio.NIOSocketServer;

public class ConcreteNIOConnectionManager<T extends SessionContract> implements NIOConnectionManager<T> {

    private class SSLChannelAttachmentWrapper implements ChannelAttachment<T> {
        private final SSLChannelAttachment<T> channelAttachment;

        public SSLChannelAttachmentWrapper(SSLChannelAttachment<T> channelAttachment) {
            this.channelAttachment = channelAttachment;
        }

        @Override
        public final void dispatchMessages() throws IOException {
            channelAttachment.dispatchMessages();
        }

        @Override
        public final List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
            List<ClientAndTheirMessage<T>> answer = new LinkedList<>();

            var allClientsAndMessages = channelAttachment.readIncomingMessages();

            for (var clientAndMessage : allClientsAndMessages) {
                assert clientAndMessage.message() instanceof EncryptedMessage;

                switch (clientAndMessage.message()) {
                    case final PortInfoRequest portInfoReq -> {
                        var sslSender = channelAttachment.getSender();

                        final var portInfoResponse = (Message.EncryptedMessage) objectToMessageDecoder
                                .decodeFromRecord(new PortInfoResponse.Payload(udpMessageSender.getPort(),
                                        tcpMessageSender.getPort()));

                        sslSender.sendMessage(portInfoResponse);
                    }

                    default -> {
                        answer.add(clientAndMessage);
                    }
                }
            }

            return answer;
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

    private final IDatabaseManager databaseManager;
    private final AuthenticationService authenticationService;

    public ConcreteNIOConnectionManager(final IDatabaseManager databaseManager,
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

        // TODO: this is odd - think about this
        // registerSocketServer(udpServer);
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
                    var attachment = (ChannelAttachment<?>) key.attachment();
                    attachment.dispatchMessages();
                }

                // TODO: think about refactoring these continues out
                if (key.isReadable()) {
                    final var clientSocketChannel = (ReadableByteChannel) key.channel();

                    // if user was not authorized yet
                    if (unauthorizedChannels.containsKey(clientSocketChannel)) {
                        handleUnauthorizedUser(clientSocketChannel, key);
                        continue;
                    }

                    // NOTE: This is faster than casting each element
                    @SuppressWarnings("unchecked")
                    var attachment = (ChannelAttachment<T>) key.attachment();

                    answer.addAll(attachment.readIncomingMessages());
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

    private final void handleUnauthorizedUser(final ReadableByteChannel clientSocketChannel, final SelectionKey key)
            throws IOException {
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

            final var wrappedAttachment = new SSLChannelAttachment<T>(key, session);
            final var sslSender = wrappedAttachment.getSender();

            final var attachment = new SSLChannelAttachmentWrapper(wrappedAttachment);

            // final var attachment = new ChannelAttachment<T, EncryptedMessage>(session,
            // new ArrayDeque<>(), bytesAccumulator, consumer);

            // final var sslSender = new QueueInserter<EncryptedMessage>(
            // attachment.messageQueue(), key);

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

    private static final int initialInterestSet = SelectionKey.OP_READ;

    private final void handleIncomingConnection(final SelectionKey key) throws IOException {
        switch (key.channel()) {
            case ServerSocketChannel serverSocketChannel -> {
                final var clientSocketChannel = serverSocketChannel.accept();
                clientSocketChannel.configureBlocking(false);

                // New user connected to SSLServer - mark them as unauthorized
                if (Objects.equals(serverSocketChannel, sslMessageSender.getServerSocketChannel())) {
                    unauthorizedChannels.put(clientSocketChannel, new SSLSocketBytesAccumulator());
                    sslMessageSender.acceptClient(clientSocketChannel);

                    clientSocketChannel.register(selector, initialInterestSet);

                } else if (Objects.equals(serverSocketChannel, tcpMessageSender.getServerSocketChannel())) {

                    var clientKey = clientSocketChannel.register(selector, 0);
                    var attachment = new TCPChannelAttachment<T>(clientKey, authenticationService, sessionCreator,
                            messageDecoder);
                    clientKey.attach(attachment);
                    clientKey.interestOpsOr(initialInterestSet);

                } else {
                    throw new IllegalStateException("Unexpected serverSocketChannel for this key");
                }

                logger.info(String.format("Client connected %s", clientSocketChannel.getRemoteAddress()));
            }

            case DatagramChannel datagramChannel -> {
                throw new IllegalStateException("DatagramChannel never accepts anything");
            }

            default -> {
                logger.severe("An error occured");
                throw new IllegalStateException("Illegal type of selected channel %s".formatted(key.channel()));
            }
        }
    }

    public void registerSocketServer(final NIOSocketServer nioSocketServer) throws IOException {
        nioSocketServer.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);
    }
}
