package network.messages;

import java.io.IOException;
import java.io.OutputStream;

public interface Message {
  void encodeAndWrite(OutputStream out) throws IOException;

}
