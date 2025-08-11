package lobby;

import java.util.Collection;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import user.IMatchmakingUserHandle;

public class LobbyFactory {
    public static Lobby createLobby(Collection<IMatchmakingUserHandle> players) {
        GameSessionManager sessionManager = GameSessionFactory.createGameSessionManager(players.stream()
                .map(IMatchmakingUserHandle::getPlayerData)
                .toList());
        Lobby lobby = new Lobby(players, sessionManager);
        for (IMatchmakingUserHandle player : players) {
            var subscribableConnector = player.getPlayerData().connector();
            player.gameStarted(sendable -> lobby.processSendable(subscribableConnector, sendable));
        }

        return lobby;
    }
}
