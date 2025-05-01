package network.messages;

import java.io.IOException;

import network.messages.utils.DataReceiver;

public interface Message {
  void encodeAndWrite(DataReceiver out) throws IOException;

}
