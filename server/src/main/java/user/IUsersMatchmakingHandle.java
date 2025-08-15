package user;

public interface IUsersMatchmakingHandle {
    enum JoinGameRequestResult {
        REQUEST_SUCCESSFUL,
        ALREADY_IN_ROOM,
        // ...
    }

    record MatchmakingParameters() {
    }

    JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters);

    /**
     * {@code true} if game lookup was in progress
     */
    boolean interruptGameLookup();
}
