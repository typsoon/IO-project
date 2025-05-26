package network.client;

import java.io.IOException;
import java.util.Optional;

import network.ConnectionData;
import network.messages.Message;
import network.messages.Sendable;

public interface ClientSideSocketWrapper {
    public static enum EstablishConnectionResult {
        ESTABLISHED, FAILED
    }

    public static class ConnectionEndedException extends Exception {
    }

    Sendable getSendable() throws IOException;

    /**
     * Will throw IllegalStateException if the sender was not yet connected
     */
    <U extends Message> Optional<U> dispatchMessage(Message message) throws IOException, ConnectionEndedException;

    EstablishConnectionResult establishConnection(ConnectionData connectionData);
}
