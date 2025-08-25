package matchmaking.lobby;

import game.session.GameSessionFactory;
import game.session.GameSessionManager;
import matchmaking.MatchmakingParameters;
import user.IMatchmakingUserHandle;

import java.util.Collection;

public record PendingLobby(Collection<LobbyMember> members,
                           MatchmakingParameters matchmakingParameters) {

    Lobby finalizeLobby() {
        GameSessionManager sessionManager = GameSessionFactory.createGameSessionManager(members.stream()
                .map(LobbyMember::matchmakingUserHandle)
                .map(IMatchmakingUserHandle::getPlayerData)
                .toList());

        var lobby = new Lobby(members, sessionManager);
        for (LobbyMember member : members) {
            var subscribableConnector = member.matchmakingUserHandle().getPlayerData().connector();
            member.matchmakingUserHandle().
                    gameStarted(sendable -> lobby.processSendable(subscribableConnector, sendable));
        }
        return lobby;
    }
}
