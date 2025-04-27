package network;

import java.io.IOException;

import network.socketwrappers.DuplexSocket;

public interface ServerSocketWrapper extends AutoCloseable {
  DuplexSocket acceptClient() throws IOException;
}
