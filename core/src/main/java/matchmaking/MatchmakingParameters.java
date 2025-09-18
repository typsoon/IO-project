package matchmaking;

import utils.ISendable;

public record MatchmakingParameters(int numPlayers) implements ISendable {
}
