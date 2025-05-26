package session;

import java.util.logging.Logger;

import network.MessageDispatcher;
import network.messages.Message;
import network.server.nio.NIOConnectionManager.SessionConcract;

public class ClientData implements SessionConcract {
    private final MessageDispatcher messageDispatcher;

    public ClientData(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void handleMessage(Message message) {
        Logger.getGlobal().info("Received message %s".formatted(message));
    }
}
