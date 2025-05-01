package network.messages.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class OutputStreamDataReceiver implements DataReceiver {
  private final NoCloseDataOutputStream out;
  private static final Charset charset = StandardCharsets.UTF_8;

  public OutputStreamDataReceiver(OutputStream outputStream) {
    this.out = new NoCloseDataOutputStream(outputStream);
  }

  @Override
  public void putInt(int val) throws IOException {
    out.writeInt(val);
  }

  @Override
  public void putByte(byte val) throws IOException {
    out.writeByte(val);
  }

  @Override
  public void putString(String str) throws IOException {
    out.writeByte(str.length());
    out.write(str.getBytes(charset));
  }

}

/**
 * This class exists because I want to write data e.g to from InputStream
 * without getting
 * linter errors (I know that I can disable them but I chose this way) and
 * closing the underlying InputStream
 */
class NoCloseDataOutputStream extends DataOutputStream {
  protected NoCloseDataOutputStream(OutputStream out) {
    super(out);
  }

  @Override
  public void close() throws IOException {
  }
}
