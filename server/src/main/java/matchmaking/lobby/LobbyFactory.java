package matchmaking.lobby;

import java.util.UUID;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import network.messages.gameplaystate.ExitTheGameMessage;
import user.IMatchmakingUserHandle;

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
                    .gameStarted(sendable -> {
                        lobby.processSendable(subscribableConnector, sendable);
                        if (sendable instanceof ExitTheGameMessage.Payload) {
                            member.matchmakingUserHandle().moveToDefaultState();
                        }
                    });
        }
        return lobby;
    }
}
