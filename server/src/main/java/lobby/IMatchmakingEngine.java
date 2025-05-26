package lobby;

import user.IUserHandle;

import java.util.Collection;

public interface IMatchmakingEngine {
    void findGame(IUserHandle user, int lobbySize);

    void findGame(Collection<IUserHandle> room, int lobbySize);
}
