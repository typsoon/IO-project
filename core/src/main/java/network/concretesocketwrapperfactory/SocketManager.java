package network.concretesocketwrapperfactory;

import java.io.IOException;
import java.net.Socket;

import network.utils.ConnectionData;

public interface SocketManager {
    Socket getSSLConnection(ConnectionData connectionData) throws IOException;
}
