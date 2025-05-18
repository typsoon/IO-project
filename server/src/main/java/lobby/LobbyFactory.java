package lobby;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import user.UserHandle;

import java.util.Collection;

public class LobbyFactory {
    public static Lobby createLobby(Collection<UserHandle> players) {
        GameSessionManager sessionManager = GameSessionFactory.createGameSessionManager(players.stream()
                .map(UserHandle::getPlayerData)
                .toList());
        Lobby lobby = new Lobby(players, sessionManager);
        for (UserHandle player : players) {
            player.setLobby(lobby);
        }

        return lobby;
    }

}
