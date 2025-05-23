package session;

import network.socketwrappers.SocketTypes.DuplexSocket;

public interface AbstractSessionFactory {
    Session getSession(DuplexSocket clientSocket);
}
