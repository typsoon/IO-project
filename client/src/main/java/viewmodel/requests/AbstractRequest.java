package viewmodel.requests;

import network.ConnectionData;

            //TODO: remove
public abstract class AbstractRequest {
    private final ConnectionData connectionData;

    protected AbstractRequest(ConnectionData connectionData) {
        this.connectionData = connectionData;
    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }

}
