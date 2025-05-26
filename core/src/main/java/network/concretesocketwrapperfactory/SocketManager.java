package network.concretesocketwrapperfactory;

import java.io.IOException;

import network.ConnectionData;
import java.net.Socket;

public interface SocketManager {
    Socket getSSLConnection(ConnectionData connectionData) throws IOException;
}
