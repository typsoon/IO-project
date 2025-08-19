package network.client;

import java.io.IOException;
import java.util.Collection;

import network.messages.Message;
import game.utility.ISendable;
import network.utils.ConnectionData;

public interface ClientSideSocketWrapper extends AutoCloseable {
    enum EstablishConnectionResult {
        ESTABLISHED, FAILED
    }

    class ConnectionEndedException extends Exception {
    }

    Collection<ISendable> getSendables() throws IOException;
    /**
     * Will throw IllegalStateException if the sender was not yet connected
     */
    void dispatchMessage(Message message) throws IOException, ConnectionEndedException;

    EstablishConnectionResult establishConnection(ConnectionData connectionData);
}
