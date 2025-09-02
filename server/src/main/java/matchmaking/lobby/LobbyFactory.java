package matchmaking.lobby;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import user.IMatchmakingUserHandle;

import java.util.UUID;

public class LobbyFactory {
    public static Lobby create(PendingLobby pendingLobby, UUID lobbyId) {
        GameSessionManager sessionManager = GameSessionFactory.createGameSessionManager(pendingLobby.members().stream()
                .map(LobbyMember::matchmakingUserHandle)
                .map(IMatchmakingUserHandle::getPlayerData)
                .toList());

        var lobby = new Lobby(pendingLobby.members(), sessionManager, lobbyId);
        for (LobbyMember member : pendingLobby.members()) {
            var subscribableConnector = member.matchmakingUserHandle().getPlayerData().connector();
            member.matchmakingUserHandle()
                    .gameStarted(sendable -> lobby.processSendable(subscribableConnector, sendable));
        }
        return lobby;
    }
}
