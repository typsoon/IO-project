package user;

import matchmaking.MatchmakingParameters;

public interface IUsersMatchmakingHandle {
    enum JoinGameRequestResult {
        REQUEST_SUCCESSFUL,
        ALREADY_IN_ROOM,
        // ...
    }

    JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters);

    boolean interruptGameLookup();
}
