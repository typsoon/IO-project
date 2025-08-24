package matchmaking.lobby;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import user.IMatchmakingUserHandle;

import java.util.Collection;

public class LobbyFactory {
    public static Lobby createLobby(Collection<LobbyMember> players) {
        GameSessionManager sessionManager = GameSessionFactory.createGameSessionManager(players.stream()
                .map(LobbyMember::matchmakingUserHandle)
                .map(IMatchmakingUserHandle::getPlayerData)
                .toList());
        Lobby lobby = new Lobby(players, sessionManager);
        for (LobbyMember player : players) {
            var subscribableConnector = player.matchmakingUserHandle().getPlayerData().connector();
            player.matchmakingUserHandle().gameStarted(
                    sendable -> lobby.processSendable(subscribableConnector, sendable)
            );
        }

        return lobby;
    }
}
