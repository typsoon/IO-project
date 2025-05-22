package lobby;

import game.actions.Action;
import game.ActionReceiver;
import game.PlayerConnector;
import game.PlayerData;
import game.session.GameSessionManager;
import user.UserHandle;

import java.util.Collection;

public class Lobby implements ActionReceiver {
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
    public void sendAction(PlayerConnector player, Action action) {
        sessionManager.sendAction(player, action);
    }
}
