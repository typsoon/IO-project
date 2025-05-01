package session;

import network.socketwrappers.DuplexSocket;

public interface AbstractSessionFactory {
  Session getSession(DuplexSocket clientSocket);
}
