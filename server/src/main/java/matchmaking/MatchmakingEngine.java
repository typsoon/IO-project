package matchmaking;

import database.IDatabaseManager.UserId;
import matchmaking.lobby.Lobby;
import matchmaking.lobby.LobbyMember;
import matchmaking.lobby.PendingLobby;
import matchmaking.pool.IMatchmakingPool;
import matchmaking.pool.IMatchmakingPoolFactory;
import user.UserState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class MatchmakingEngine implements IMatchmakingEngine {
    private final List<Lobby> lobbies = new ArrayList<>();

    private final Map<MatchmakingParameters, IMatchmakingPool> pools = new ConcurrentHashMap<>();
    private final Map<UserId, Set<MatchmakingParameters>> activeSearches = new ConcurrentHashMap<>();

    private final Consumer<PendingLobby> pendingLobbyConsumer;
    private final IMatchmakingPoolFactory matchmakingPoolFactory;

    MatchmakingEngine(Consumer<PendingLobby> pendingLobbyConsumer,
                      IMatchmakingPoolFactory matchmakingPoolFactory) {
        this.pendingLobbyConsumer = pendingLobbyConsumer;
        this.matchmakingPoolFactory = matchmakingPoolFactory;
    }

    @Override
    public void findGame(LobbyMember user, MatchmakingParameters matchmakingParameters) {
        var pool = getPool(matchmakingParameters);
        pool.add(user);
        activeSearches.computeIfAbsent(user.userView().id(),
                k -> new HashSet<>()).add(matchmakingParameters);
        var maybePendingLobby = pool.tryFormLobby();
        maybePendingLobby.ifPresent(pendingLobbyConsumer);
        user.userState().setState(UserState.State.SEARCHING);
    }

    @Override
    public void createGame(Collection<LobbyMember> room, MatchmakingParameters matchmakingParameters) {
        var pendingLobby = new PendingLobby(room, matchmakingParameters);
        pendingLobbyConsumer.accept(pendingLobby);
    }

    @Override
    public boolean interruptSearch(UserId userId) {
        var paramsSet = activeSearches.remove(userId);
        if (paramsSet == null)
            return false;
        for (var params : paramsSet) {
            var pool = getPool(params);
            pool.remove(userId);
        }
        return true;
    }

    private IMatchmakingPool getPool(MatchmakingParameters parameters) {
        return pools.computeIfAbsent(parameters,
                k -> matchmakingPoolFactory.createPool(parameters));
    }

    public void finalizeLobby(Lobby lobby) {
        synchronized (lobbies) {
            lobbies.add(lobby);
        }
    }
}
