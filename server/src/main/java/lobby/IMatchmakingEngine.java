package lobby;

import user.IMatchmakingUserHandle;

import java.util.Collection;

public interface IMatchmakingEngine {
    void findGame(IMatchmakingUserHandle user, int lobbySize);

    void findGame(Collection<IMatchmakingUserHandle> room, int lobbySize);
}
