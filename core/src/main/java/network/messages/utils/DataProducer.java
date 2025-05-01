package network.messages.utils;

import java.io.IOException;

public interface DataProducer {
  int getInt() throws IOException;

  byte getByte() throws IOException;

  String getString() throws IOException;
}
