package lobby;

import java.util.Collection;
import java.util.logging.Logger;

import game.actions.IAction;
import game.session.IActionReceiver;
import game.session.ISubscribablePlayerConnector;
import game.session.PlayerData;
import game.utility.ISendable;
import user.IMatchmakingUserHandle;

public class Lobby {
    private final Collection<IMatchmakingUserHandle> members;
    private final IActionReceiver sessionManager;

    public Lobby(Collection<IMatchmakingUserHandle> members, IActionReceiver sessionManager) {
        this.members = members;
        this.sessionManager = sessionManager;
    }

    public Collection<PlayerData> getPlayerData() {
        return members.stream()
                .map(IMatchmakingUserHandle::getPlayerData)
                .toList();
    }

    void processSendable(ISubscribablePlayerConnector playerConnector, ISendable sendable) {
        switch (sendable) {
            case IAction action -> sessionManager.sendAction(playerConnector, action);
            default -> {
                Logger.getGlobal().info("Unexpected sendable %s".formatted(sendable));
            }
        }
    }
}
