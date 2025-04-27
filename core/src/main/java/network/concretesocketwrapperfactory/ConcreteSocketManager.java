package network.concretesocketwrapperfactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import network.ConnectionData;
import java.util.Map;

class ConcreteSocketManager implements SocketManager {
  private final Map<ConnectionData, Socket> activeConnections = new HashMap<>();

  private Socket establishConnection(ConnectionData connectionData) throws IOException {
    return new Socket(connectionData.host(), connectionData.port());
  }

  @Override
  public Socket getConnection(ConnectionData connectionData) throws IOException {
    if (!activeConnections.containsKey(connectionData)) {
      var val = establishConnection(connectionData);
      activeConnections.put(connectionData, val);
      return val;
    }
    return activeConnections.get(connectionData);
  }
}
