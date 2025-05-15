package network.messages.loginstate;

import java.io.IOException;
import java.util.Optional;

import network.messages.Message;
import network.messages.utils.DataReceiver;

public record LogInResponse(Optional<Integer> authTokenOptional) implements Message {
    public static int NO_AUTH_TOKEN = -1;

    @Override
    public void encodeAndWrite(DataReceiver out) throws IOException {
        byte msgSize = Byte.BYTES + Integer.BYTES;
        out.putByte(msgSize);
        out.putByte((byte) 1);
        out.putInt(authTokenOptional.orElse(NO_AUTH_TOKEN));
    }
}
