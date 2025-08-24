package matchmaking;

import database.IDatabaseManager;
import matchmaking.lobby.LobbyMember;

import java.util.Collection;

public interface IMatchmakingEngine {
    void findGame(LobbyMember user, MatchmakingParameters matchmakingParameters);

    void createGame(Collection<LobbyMember> room, MatchmakingParameters matchmakingParameters);

    boolean interruptSearch(IDatabaseManager.UserId userId);
}
