package network.messages.loginstate;

import java.io.IOException;
import java.util.Optional;

import network.messages.Message.EncryptedMessage;
import game.utility.ISendable;
import network.messages.utils.DataConsumer;

public final class LogInResponse extends EncryptedMessage {
    public static int NO_AUTH_TOKEN = -1;
    public static final byte id = 1;

    public record Payload(Optional<Integer> authToken) implements ISendable {
    }

    private final Payload authTokenOptional;

    public LogInResponse(Optional<Integer> authTokenOptional) {
        this.authTokenOptional = new Payload(authTokenOptional);
    }

    @Override
    public void encodeAndWrite(DataConsumer out) throws IOException {
        byte msgSize = Byte.BYTES + Integer.BYTES;
        out.putByte(msgSize);
        out.putByte(id);
        out.putInt(authTokenOptional.authToken.orElse(NO_AUTH_TOKEN));
    }

    @Override
    public ISendable getSendable() {
        return authTokenOptional;
    }
}
