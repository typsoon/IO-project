package lobby;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import user.IUserHandle;

import java.util.Collection;

public class LobbyFactory {
    public static Lobby createLobby(Collection<IUserHandle> players) {
        GameSessionManager sessionManager = GameSessionFactory.createGameSessionManager(players.stream()
                .map(IUserHandle::getPlayerData)
                .toList());
        Lobby lobby = new Lobby(players, sessionManager);
        for (IUserHandle player : players) {
            player.gameStarted(lobby);
        }

        return lobby;
    }
}
