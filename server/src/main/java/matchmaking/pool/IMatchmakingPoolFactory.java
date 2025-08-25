package matchmaking.pool;

import matchmaking.MatchmakingParameters;

public interface IMatchmakingPoolFactory {
    IMatchmakingPool createPool(MatchmakingParameters matchmakingParameters);
}
