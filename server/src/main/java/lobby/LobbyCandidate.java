package lobby;

import user.IMatchmakingUserHandle;
import user.IUsersMatchmakingHandle;

import java.util.Collection;

public record LobbyCandidate(Collection<IMatchmakingUserHandle> members,
                             IUsersMatchmakingHandle.MatchmakingParameters matchmakingParameters) {
}
