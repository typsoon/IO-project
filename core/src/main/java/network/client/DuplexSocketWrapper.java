package network.client;

import java.io.IOException;
import java.util.Collection;

import game.utility.ISendable;
import network.messages.Message;

public interface DuplexSocketWrapper {
    class ConnectionEndedException extends Exception {
    }

    Collection<ISendable> getSendables() throws IOException;

    void dispatchMessage(Message message) throws IOException, ConnectionEndedException;
}
