package network.client;

import network.utils.ConnectionData;

public interface ClientSideSocketWrapper extends AutoCloseable, DuplexSocketWrapper {
    /**
     * Dispatch message will throw IllegalStateException if the sender was not yet
     * connected
     */

    enum EstablishConnectionResult {
        ESTABLISHED, FAILED
    }

    EstablishConnectionResult establishConnection(ConnectionData connectionData);
}
