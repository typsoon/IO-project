package session.receivers;

import java.util.logging.Logger;

import game.session.ISendableConsumer;
import game.utility.ISendable;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;

public class ConfigurationStateConsumer implements ISendableConsumer {
    private final IUsersRoomHandle userRoomHandle;
    private final IUsersMatchmakingHandle matchmakingHandle;

    public ConfigurationStateConsumer(IUsersRoomHandle userRoomHandle, IUsersMatchmakingHandle matchmakingHandle) {
        this.userRoomHandle = userRoomHandle;
        this.matchmakingHandle = matchmakingHandle;
    }

    @Override
    public void processSendable(final ISendable sendable) {
        Logger.getGlobal().info("Received sendable %s".formatted(sendable));
    }

}
