package lobby;

import game.actions.IAction;
import game.session.IActionReceiver;
import game.session.IPlayerConnector;
import game.session.PlayerData;
import game.session.GameSessionManager;
import user.UserHandle;

import java.util.Collection;

public class Lobby implements IActionReceiver {
    private final Collection<UserHandle> members;
    private final GameSessionManager sessionManager;

    public Lobby(Collection<UserHandle> members, GameSessionManager sessionManager) {
        this.members = members;
        this.sessionManager = sessionManager;
    }

    public Collection<PlayerData> getPlayerData() {
        return members.stream()
                .map(UserHandle::getPlayerData)
                .toList();
    }

    @Override
    public void sendAction(IPlayerConnector player, IAction action) {
        sessionManager.sendAction(player, action);
    }
}
