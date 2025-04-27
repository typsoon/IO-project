package viewmodel.requests;

import network.ConnectionData;

public abstract class AbstractRequest {
  private final ConnectionData connectionData;

  protected AbstractRequest(ConnectionData connectionData) {
    this.connectionData = connectionData;
  }

  public ConnectionData getConnectionData() {
    return connectionData;
  }

}
