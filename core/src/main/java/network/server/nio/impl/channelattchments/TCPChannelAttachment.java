package network.server.nio.impl.channelattchments;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import network.messages.Message.TCPMessage;
import network.messages.decoding.MessageDecoder;
import network.server.AuthenticationService;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;
import network.utils.AccumulatorAdapters;
import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.WhatWasRead;
import network.utils.impl.OrdinaryBytesAccumulator;

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

    TCPChannelAttachment(final SelectionKey key, final AuthenticationService authenticationService,
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
                Logger.getGlobal().info("Session contract %s".formatted(sessionContract));
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
