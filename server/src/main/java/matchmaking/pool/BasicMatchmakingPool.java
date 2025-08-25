package matchmaking.pool;

import database.IDatabaseManager;
import matchmaking.MatchmakingParameters;
import matchmaking.lobby.LobbyMember;
import matchmaking.lobby.PendingLobby;

import java.util.*;

public class BasicMatchmakingPool implements IMatchmakingPool {
    private final MatchmakingParameters matchmakingParameters;
    private final Queue<LobbyMember> queue = new LinkedList<>();
    private final Set<IDatabaseManager.UserId> userIds = new HashSet<>();

    BasicMatchmakingPool(MatchmakingParameters matchmakingParameters) {
        this.matchmakingParameters = matchmakingParameters;
    }

    @Override
    public synchronized void add(LobbyMember user) {
        var userId = user.userView().id();
        if (userIds.add(userId)) queue.add(user);
    }

    @Override
    public synchronized boolean remove(IDatabaseManager.UserId userId) {
        if (userIds.remove(userId)) {
            queue.removeIf(member -> member.userView().id().equals(userId));
            return true;
        }
        return false;
    }

    @Override
    public synchronized Optional<PendingLobby> tryFormLobby() {
        if (queue.size() < matchmakingParameters.numPlayers()) return Optional.empty();
        var members = new ArrayList<LobbyMember>(matchmakingParameters.numPlayers());
        for (int i = 0; i < matchmakingParameters.numPlayers(); i++) {
            var member = queue.poll();
            members.add(member);
            assert member != null;
            userIds.remove(member.userView().id());
        }
        var pendingLobby = new PendingLobby(members, matchmakingParameters);
        return Optional.of(pendingLobby);
    }

    @Override
    public synchronized int size() {
        return queue.size();
    }

    @Override
    public synchronized boolean contains(IDatabaseManager.UserId userId) {
        return userIds.contains(userId);
    }
}
