package lobby;

import user.IMatchmakingUserHandle;

import java.util.Collection;

public interface IMatchmakingEngine {
    void findGame(IMatchmakingUserHandle user, int lobbySize);

    void createGame(Collection<IMatchmakingUserHandle> room);
}
