package user;

public class MatchmakingHandle implements IMatchmakingHandle {
    @Override
    public JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters) {
        return null;
    }

    @Override
    public boolean interruptGameLookup() {
        return false;
    }
}
