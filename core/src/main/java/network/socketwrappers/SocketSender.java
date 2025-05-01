package network.socketwrappers;

import java.io.IOException;
import java.util.Optional;

import network.messages.Message;

public interface SocketSender<SentMessage extends Message, ReceivedMessage extends Message> {
  /**
   * @param message
   * @return a response if it's expected
   */
  Optional<ReceivedMessage> sendMessage(SentMessage message) throws IOException;
}
