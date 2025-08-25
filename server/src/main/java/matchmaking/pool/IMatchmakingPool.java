package matchmaking.pool;

import database.IDatabaseManager.UserId;
import matchmaking.lobby.LobbyMember;
import matchmaking.lobby.PendingLobby;

import java.util.Optional;

public interface IMatchmakingPool {
    void add(LobbyMember user);

    boolean remove(UserId userId);

    Optional<PendingLobby> tryFormLobby();

    int size();

    boolean contains(UserId userId);
}
