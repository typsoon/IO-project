package network.messages.loginstate;

import java.io.OutputStream;
import java.io.IOException;

import network.messages.Message;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public record LogInQuery(String username, String password) implements Message {
  // TODO: import charset from config
  private static Charset charset = StandardCharsets.UTF_8;

  private void writeString(OutputStream out, String s) throws IOException {
    writeNum(out, (byte) s.length());
    var bytes = s.getBytes(charset);
    if (bytes.length > Byte.MAX_VALUE) {
      throw new IOException("String too long");
    }
    out.write(bytes);
  }

  private void writeNum(OutputStream out, byte val) throws IOException {
    out.write(val);
  }

  @Override
  public void encodeAndWrite(OutputStream out) throws IOException {
    // TODO: change this 0 later
    writeNum(out, (byte) 0);
    writeString(out, username);
    writeString(out, password);
  }
}
