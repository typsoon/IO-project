package network.client;

import java.io.IOException;
import java.util.Collection;

import network.messages.Message;
import utils.ISendable;

public interface DuplexSocketWrapper {
    class ConnectionEndedException extends Exception {
    }

    Collection<ISendable> getSendables() throws IOException;

    void dispatchMessage(Message message) throws IOException, ConnectionEndedException;
}
