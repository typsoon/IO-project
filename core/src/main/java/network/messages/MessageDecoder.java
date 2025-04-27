package network.messages;

import java.io.IOException;
import java.io.InputStream;

public interface MessageDecoder {
  Message decodeMessage(InputStream in) throws IOException;
}
