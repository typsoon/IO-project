package session.receivers;

import java.util.logging.Logger;

import game.session.ISendableConsumer;
import game.utility.ISendable;
import user.IMatchmakingHandle;
import user.IUserRoomHandle;

public class ConfigurationStateConsumer implements ISendableConsumer {
    private final IUserRoomHandle userRoomHandle;
    private final IMatchmakingHandle matchmakingHandle;

    public ConfigurationStateConsumer(IUserRoomHandle userRoomHandle, IMatchmakingHandle matchmakingHandle) {
        this.userRoomHandle = userRoomHandle;
        this.matchmakingHandle = matchmakingHandle;
    }

    @Override
    public void processSendable(final ISendable sendable) {
        Logger.getGlobal().info("Received sendable %s".formatted(sendable));
    }

}
