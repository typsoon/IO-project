package network.messages.loginstate;

import java.io.IOException;

import network.messages.Message;
import network.messages.MessagesConfig;
import network.messages.utils.DataConsumer;

import java.nio.charset.Charset;

import game.actions.Action;

public final class LogInQuery extends Message {
    // TODO: import charset from config
    public static final byte id = 0;
    private static Charset charset = MessagesConfig.msgCharset;
    private final String username, password;

    public LogInQuery(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void encodeAndWrite(DataConsumer out) throws IOException {
        var usernameBytes = username.getBytes(charset);
        var passwordBytes = password.getBytes(charset);

        byte messageSize = (byte) (Byte.BYTES + 2 * Byte.BYTES + usernameBytes.length + passwordBytes.length);

        out.putByte(messageSize);

        out.putByte(id);

        out.putString(username);
        out.putString(password);
    }

    @Override
    public Action getAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAction'");
    }

    public static Charset getCharset() {
        return charset;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
