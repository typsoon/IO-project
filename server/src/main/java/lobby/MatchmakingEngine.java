package lobby;

import room.Room;
import user.UserHandle;

import java.util.Collection;

public interface MatchmakingEngine {
    void findGame(UserHandle user, int lobbySize);

    void findGame(Collection<UserHandle> room, int lobbySize);
}
