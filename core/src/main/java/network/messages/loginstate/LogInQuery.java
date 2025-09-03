package network.messages.loginstate;

import java.io.IOException;
import java.nio.charset.Charset;

import network.messages.Message.EncryptedMessage;
import network.messages.MessagesConfig;
import network.messages.utils.DataConsumer;
import network.utils.Credentials;
import utils.ISendable;

public final class LogInQuery extends EncryptedMessage {
    // TODO: import charset from config
    public static final byte id = 0;
    private static Charset charset = MessagesConfig.msgCharset;
    private final String login, password;

    public LogInQuery(String username, String password) {
        this.login = username;
        this.password = password;
    }

    @Override
    public void encodeAndWrite(DataConsumer out) throws IOException {
        var usernameBytes = login.getBytes(charset);
        var passwordBytes = password.getBytes(charset);

        byte messageSize = (byte) (Byte.BYTES + 2 * Byte.BYTES + usernameBytes.length + passwordBytes.length);

        out.putByte(messageSize);

        out.putByte(id);

        out.putString(login);
        out.putString(password);
    }

    @Override
    public ISendable getSendable() {
        return new Credentials(login, password);
    }

    public static Charset getCharset() {
        return charset;
    }

    public String username() {
        return login;
    }

    public String password() {
        return password;
    }
}
