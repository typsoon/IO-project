package network;

import java.io.IOException;

import network.socketwrappers.SocketTypes.DuplexSocket;

public interface ServerSocketWrapper extends AutoCloseable {
    DuplexSocket acceptClient() throws IOException;
}
