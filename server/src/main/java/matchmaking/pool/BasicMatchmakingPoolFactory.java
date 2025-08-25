package matchmaking.pool;

import matchmaking.MatchmakingParameters;

public class BasicMatchmakingPoolFactory implements IMatchmakingPoolFactory {
    @Override
    public IMatchmakingPool createPool(MatchmakingParameters matchmakingParameters) {
        return new BasicMatchmakingPool(matchmakingParameters);
    }
}
