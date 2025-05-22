package network.messages.loginstate;

import java.io.IOException;
import java.util.Optional;

import game.actions.Action;

import network.messages.Message;
import network.messages.utils.DataConsumer;

public final class LogInResponse extends Message {
    public static int NO_AUTH_TOKEN = -1;
    public static final byte id = 1;

    public LogInResponse(Optional<Integer> authTokenOptional) {
        this.authTokenOptional = authTokenOptional;
    }

    private final Optional<Integer> authTokenOptional;

    public final Optional<Integer> authTokenOptional() {
        return authTokenOptional;
    }

    @Override
    public void encodeAndWrite(DataConsumer out) throws IOException {
        byte msgSize = Byte.BYTES + Integer.BYTES;
        out.putByte(msgSize);
        out.putByte(id);
        out.putInt(authTokenOptional.orElse(NO_AUTH_TOKEN));
    }

    @Override
    public Action getAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAction'");
    }
}
