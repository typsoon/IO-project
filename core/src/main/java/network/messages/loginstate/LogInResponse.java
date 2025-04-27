package network.messages.loginstate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import network.messages.Message;

public record LogInResponse(Optional<Integer> authTokenOptional) implements Message {
  public static int NO_AUTH_TOKEN = -1;

  @Override
  public void encodeAndWrite(OutputStream out) throws IOException {
    try (var ds_out = new NoCloseDataOutputStream(out)) {
      ds_out.writeByte(1);
      ds_out.writeInt(authTokenOptional.orElse(NO_AUTH_TOKEN));
    }
  }
}

/**
 * This class exists because I want to write data e.g to from InputStream
 * without getting
 * linter errors (I know that I can disable them but I chose this way) and
 * closing the underlying InputStream
 */
class NoCloseDataOutputStream extends DataOutputStream {
  protected NoCloseDataOutputStream(OutputStream in) {
    super(in);
  }

  @Override
  public void close() throws IOException {
  }
}
