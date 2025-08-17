package network.server.nio;

import java.io.IOException;
import java.util.Collection;

import database.IDatabaseManager.UserId;
import network.MessageDispatcher;
import network.messages.Message;

public interface NIOConnectionManager<T extends NIOConnectionManager.SessionContract> {
    public static interface SessionContract {
        MessageDispatcher getMessageDispatcher();
    }

    // public static record ClientAndTheirMessages<T>(T client, Queue<? extends
    // Message> message) {
    // }

    public static record ClientAndTheirMessage<T>(T client, Message message) {
    }

    public static interface SessionCreator<T extends SessionContract> {
        // T getUDPSession(UserId address);

        // T getTCPSession(UserId address);

        T getSession(UserId address);
    }

    Collection<ClientAndTheirMessage<T>> select() throws IOException;

    // void registerSocketServer(NIOSocketServer nioSocketServer) throws
    // IOException;
}
