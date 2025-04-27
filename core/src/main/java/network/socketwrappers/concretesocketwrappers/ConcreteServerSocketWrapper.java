package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.ServerSocketWrapper;
import network.socketwrappers.DuplexSocket;

public class ConcreteServerSocketWrapper implements ServerSocketWrapper {
  private final ServerSocket serverSocket;

  public ConcreteServerSocketWrapper(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public DuplexSocket acceptClient() throws IOException {
    Socket clientSocket = serverSocket.accept();
    return new ClientSessionSocket(clientSocket);
  }

  @Override
  public void close() throws Exception {
    serverSocket.close();
  }
}
