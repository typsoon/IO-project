package user;

public interface IMatchmakingHandle {
    public static enum JoinGameRequestResult {
        REQUEST_SUCCESSFULL,
        ALREADY_IN_ROOM,
        // ...
    }

    public static record MatchmakingParameters() {
    }

    JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters);

    /**
     * @{code true} if game lookup was in progress
     */
    boolean interruptGameLookup();
}
