package network.messages.loginstate;

import java.io.IOException;
import java.util.Optional;

import network.messages.Message.EncryptedMessage;
import network.messages.utils.DataConsumer;
import utils.ISendable;

public final class LogInResponse extends EncryptedMessage {
    public static int NO_AUTH_TOKEN = -1;
    public static final byte id = 1;

    private final Payload payload;

    public record Payload(Optional<Integer> authToken, int userId) implements ISendable {
    }

    public LogInResponse(Optional<Integer> authTokenOptional, int userId) {
        this.payload = new Payload(authTokenOptional, userId);
    }

    @Override
    public void encodeAndWrite(DataConsumer out) throws IOException {
        byte msgSize = Byte.BYTES + Integer.BYTES;
        out.putByte(msgSize);
        out.putByte(id);
        out.putInt(payload.authToken.orElse(NO_AUTH_TOKEN));
        out.putInt(payload.userId);
    }

    @Override
    public ISendable getSendable() {
        return payload;
    }
}
