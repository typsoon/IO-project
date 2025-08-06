package network.server;

import java.io.IOException;

import network.messages.Message;
import network.socketwrappers.SocketTypes.DuplexSocket;

public interface ServerSocketWrapper extends AutoCloseable {
    DuplexSocket<Message> acceptClient() throws IOException;
}
