package session;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import network.messages.Message;

// TODO: think whether this should extend autocloseable
public interface Session {
  HandlingResult handleMessage(Message message);

  void sendResponses(WritableByteChannel channel) throws IOException;

  HandlingResult handleIncomingBytes(ReadableByteChannel byteIn) throws IOException;
}

enum HandlingResult {
  SHOULD_RESPOND,
  DONT_RESPOND,
  CONNECTION_ENDED
}
