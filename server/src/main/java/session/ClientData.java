package session;

import java.util.logging.Logger;

import network.MessageDispatcher;
import network.messages.Message;
import network.server.nio.NIOConnectionManager.SessionConcract;
import user.IUserRoomHandle;

public class ClientData implements SessionConcract {
    private final MessageDispatcher messageDispatcher;
    private final IUserRoomHandle userRoomHandle;

    public ClientData(MessageDispatcher messageDispatcher, IUserRoomHandle userRoomHandle) {
        this.messageDispatcher = messageDispatcher;
        this.userRoomHandle = userRoomHandle;
    }

    @Override
    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void handleMessage(Message message) {
        Logger.getGlobal().info("Received message %s".formatted(message));
    }
}
