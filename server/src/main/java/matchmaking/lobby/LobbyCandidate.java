package matchmaking.lobby;

import matchmaking.MatchmakingParameters;

import java.util.Collection;

public record LobbyCandidate(Collection<LobbyMember> members,
                             MatchmakingParameters matchmakingParameters) {
}
