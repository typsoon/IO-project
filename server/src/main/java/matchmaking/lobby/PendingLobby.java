package matchmaking.lobby;

import matchmaking.MatchmakingParameters;

import java.util.Collection;

public record PendingLobby(Collection<LobbyMember> members,
                           MatchmakingParameters matchmakingParameters) {
}
