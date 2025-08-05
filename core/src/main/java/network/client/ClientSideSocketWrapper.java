package network.client;

import java.io.IOException;
import java.util.Collection;

import network.ConnectionData;
import network.messages.Message;
import network.messages.Sendable;

public interface ClientSideSocketWrapper extends AutoCloseable {
    public static enum EstablishConnectionResult {
        ESTABLISHED, FAILED
    }

    public static class ConnectionEndedException extends Exception {
    }

    Collection<Sendable> getSendables() throws IOException;

    /**
     * Will throw IllegalStateException if the sender was not yet connected
     */
    void dispatchMessage(Message message) throws IOException, ConnectionEndedException;

    EstablishConnectionResult establishConnection(ConnectionData connectionData);
}
