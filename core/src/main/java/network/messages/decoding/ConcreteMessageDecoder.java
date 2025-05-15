package network.messages.decoding;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.utils.DataProducer;
import network.messages.Message;

public class ConcreteMessageDecoder implements MessageDecoder {
    private static final Logger logger = Logger.getGlobal();

    @Override
    public Message decodeMessage(DataProducer dataProducer) throws IOException {
        // byte messageLen = dataProducer.getByte();
        int messageCode = dataProducer.getByte();

        logger.finer(String.format("Received message of code: %s", messageCode));

        switch (messageCode) {
            case 0 -> {
                var username = dataProducer.getString();
                var password = dataProducer.getString();
                return new LogInQuery(username, password);
            }

            case 1 -> {
                int answer = dataProducer.getInt();
                return new LogInResponse((answer == LogInResponse.NO_AUTH_TOKEN) ? Optional.empty() : Optional.of(answer));
            }

            default -> {
                throw new IllegalStateException(String.format("Undefined message code: %d", messageCode));
            }
        }
    }
}
