package session;

import network.socketwrappers.SocketTypes.DuplexSocket;

public class ConcreteSessionFactory implements AbstractSessionFactory {
    @Override
    public Session getSession(DuplexSocket clientSocket) {
        return new ClientSession(clientSocket);
    }
}
