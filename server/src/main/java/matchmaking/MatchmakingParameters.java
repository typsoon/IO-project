package matchmaking;

public record MatchmakingParameters(int numPlayers) {
    @Override
    public int hashCode() {
        return Integer.hashCode(numPlayers);
    }
}
