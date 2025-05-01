package network.messages.loginstate;

import java.io.IOException;

import network.messages.Message;
import network.messages.utils.DataReceiver;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public record LogInQuery(String username, String password) implements Message {
  // TODO: import charset from config
  private static Charset charset = StandardCharsets.UTF_8;

  private byte[] getStrBytes(String s) throws IOException {
    var bytes = s.getBytes(charset);
    if (bytes.length > Byte.MAX_VALUE) {
      throw new IOException("String too long");
    }
    return bytes;
  }

  @Override
  public void encodeAndWrite(DataReceiver out) throws IOException {
    var usernameBytes = username().getBytes(charset);
    var passwordBytes = password().getBytes(charset);

    byte messageSize = (byte) (Byte.BYTES + 2 * Byte.BYTES + usernameBytes.length + passwordBytes.length);

    out.putByte(messageSize);

    // TODO: change this 0 later
    out.putByte((byte) 0);

    out.putString(username);
    out.putString(password);
  }
}
