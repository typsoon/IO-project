package network.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;

public class ConcreteMessageDecoder implements MessageDecoder {
  private final Charset charset = StandardCharsets.UTF_8;

  @Override
  public Message decodeMessage(InputStream in) throws IOException {
    int messageCode = in.read();

    switch (messageCode) {
      case 0 -> {

        byte usernameLen, passwordLen;
        // TODO: remove this in the future
        usernameLen = in.readNBytes(1)[0];
        var username = new String(in.readNBytes(usernameLen), charset);

        passwordLen = in.readNBytes(1)[0];
        var password = new String(in.readNBytes(passwordLen), charset);

        return new LogInQuery(username, password);
      }

      case 1 -> {
        try (var ds = new NoCloseDataInputStream(in)) {
          int answer = ds.readInt();
          return new LogInResponse((answer == LogInResponse.NO_AUTH_TOKEN) ? Optional.empty() : Optional.of(answer));
        }
      }

      default -> {
        throw new IllegalStateException("Undefined message code");
      }
    }
  }
}

/**
 * This class exists because I want to read data e.g ints from InputStream
 * without getting
 * linter errors (I know that I can disable them but I chose this way) and
 * closing the underlying InputStream
 */
class NoCloseDataInputStream extends DataInputStream {
  protected NoCloseDataInputStream(InputStream in) {
    super(in);
  }

  @Override
  public void close() throws IOException {
  }
}
