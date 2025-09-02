package matchmaking;

import matchmaking.lobby.Lobby;
import matchmaking.lobby.LobbyMember;
import matchmaking.pool.BasicMatchmakingPoolFactory;
import matchmaking.pool.IMatchmakingPoolFactory;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MatchmakingEngineFactory implements IMatchmakingEngineFactory {
    private static final long DEFAULT_CONFIRMATION_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30); // TODO: config

    private final IMatchmakingPoolFactory poolFactory = new BasicMatchmakingPoolFactory();

    @Override
    public IMatchmakingEngine create() {
        AtomicReference<MatchmakingEngine> engineRef = new AtomicReference<>();

        Consumer<Lobby> onLobbyFinalized = lobby -> {
            var engine = engineRef.get();
            if (engine != null) {
                engine.finalizeLobby(lobby);
            }
        };

        ConfirmationManager confirmationManager = getConfirmationManager(engineRef, onLobbyFinalized);

        MatchmakingEngine matchmakingEngine = new MatchmakingEngine(confirmationManager, poolFactory);
        engineRef.set(matchmakingEngine);

        return matchmakingEngine;
    }

    private static ConfirmationManager getConfirmationManager(AtomicReference<MatchmakingEngine> engineRef,
            Consumer<Lobby> onLobbyFinalized) {
        BiConsumer<Collection<LobbyMember>, MatchmakingParameters> requeueHandler = (members, params) -> {
            var engine = engineRef.get();
            if (engine != null) {
                for (var member : members) {
                    engine.findGame(member, params);
                }
            }
        };

        return new ConfirmationManager(
                onLobbyFinalized,
                requeueHandler,
                DEFAULT_CONFIRMATION_TIMEOUT_MS);
    }
}
