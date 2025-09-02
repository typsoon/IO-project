package matchmaking.lobby;

import game.actions.IAction;
import game.session.IActionReceiver;
import game.session.ISubscribablePlayerConnector;
import game.session.PlayerData;
import game.utility.ISendable;
import user.IMatchmakingUserHandle;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

public class Lobby {
    private final UUID lobbyId;
    private final Collection<LobbyMember> members;
    private final IActionReceiver sessionManager;

    public Lobby(Collection<LobbyMember> members, IActionReceiver sessionManager, UUID lobbyId) {
        this.members = members;
        this.sessionManager = sessionManager;
        this.lobbyId = lobbyId;
    }

    public Collection<PlayerData> getPlayerData() {
        return members.stream()
                .map(LobbyMember::matchmakingUserHandle)
                .map(IMatchmakingUserHandle::getPlayerData)
                .toList();
    }

    void processSendable(ISubscribablePlayerConnector playerConnector, ISendable sendable) {
        switch (sendable) {
            case IAction action -> {
                sessionManager.sendAction(playerConnector, action);
            }
            default -> {
                Logger.getGlobal().info("Unexpected sendable %s".formatted(sendable));
            }
        }
    }
}
