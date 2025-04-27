package session;

import network.socketwrappers.DuplexSocket;

public class ConcreteSessionFactory implements AbstractSessionFactory {

  @Override
  public Session getSession(DuplexSocket clientSocket) {
    return new ClientSession(clientSocket);
  }

}
