package network.concretesocketwrapperfactory;

import java.io.IOException;
import java.net.Socket;

import network.ConnectionData;

public interface SocketManager {

  Socket getConnection(ConnectionData connectionData) throws IOException;
}
