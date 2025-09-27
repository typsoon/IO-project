package network.server.nio.impl.channelattchments;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.ByteBufferDataProducer;
import network.server.AuthenticationService;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.utils.BytesAccumulator;

public interface ChannelAttachment<T extends SessionContract> {
    public enum State {
        BEFORE_VALIDATION,
        AFTER_VALIDATION
    }

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
