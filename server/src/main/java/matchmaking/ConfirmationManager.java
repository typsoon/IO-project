package matchmaking;

import database.IDatabaseManager.UserId;
import game.session.ISendableConsumer;
import gameclient.user.IUserView;
import matchmaking.lobby.Lobby;
import matchmaking.lobby.LobbyFactory;
import matchmaking.lobby.LobbyMember;
import matchmaking.lobby.PendingLobby;
import network.messages.userstate.GameConfirmation;
import user.UserState;
import utils.ISendable;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConfirmationManager implements Consumer<PendingLobby> {
    private final ScheduledExecutorService scheduler;
    private final long timeoutMillis;

    private final Consumer<Lobby> onLobbyFinalized;
    private final BiConsumer<Collection<LobbyMember>, MatchmakingParameters> requeueHandler;

    private final Map<UUID, ActivePending> activePendings = new ConcurrentHashMap<>();

    ConfirmationManager(Consumer<Lobby> onLobbyFinalized,
            BiConsumer<Collection<LobbyMember>, MatchmakingParameters> requeueHandler,
            long timeoutMillis) {
        this.onLobbyFinalized = onLobbyFinalized;
        this.requeueHandler = requeueHandler;
        this.timeoutMillis = timeoutMillis;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ConfirmationManagerScheduler");
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void accept(PendingLobby pendingLobby) {
        for (var member : pendingLobby.members()) {
            member.userState().setState(UserState.State.MATCHED_PENDING_CONFIRM);
        }
        UUID pid = UUID.randomUUID();
        ActivePending activePending = new ActivePending(pid, pendingLobby, Instant.now());
        if (activePendings.putIfAbsent(pid, activePending) != null)
            return; // very unlikely

        for (var member : pendingLobby.members()) {
            var id = member.userView().id();
            member.matchmakingUserHandle().moveToConfirmationState(new ConfirmationConsumer(id, pid));
        }

        ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> onTimeout(pid),
                timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        activePending.setScheduledFuture(scheduledFuture);
    }

    private void onTimeout(UUID pid) {
        var activePending = activePendings.remove(pid);
        if (activePending == null)
            return;
        activePending.markTimeout();
        resolve(activePending);
    }

    private void resolve(ActivePending activePending) {
        if (!activePending.markResolving())
            return;
        activePending.cancelTimeout();

        var acceptedMembers = activePending.getAcceptedMembers();
        var declinedMembers = activePending.getDeclinedMembers();
        var params = activePending.pendingLobby.matchmakingParameters();

        if (declinedMembers.isEmpty()) {
            onLobbyFinalized.accept(activePending.finalizeLobby());
        } else {
            requeueHandler.accept(acceptedMembers, params);
        }
        activePendings.remove(activePending.lobbyId);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    class ConfirmationConsumer implements ISendableConsumer {
        final UserId userId;
        final UUID lobbyId;

        ConfirmationConsumer(UserId userId, UUID lobbyId) {
            this.userId = userId;
            this.lobbyId = lobbyId;
        }

        @Override
        public void processSendable(ISendable sendable) {
            if (sendable instanceof GameConfirmation(GameConfirmation.Confirmation confirmation)) {
                boolean confirmed = confirmation == GameConfirmation.Confirmation.CONFIRMED;
                var ap = activePendings.get(lobbyId);
                if (ap == null)
                    return;
                boolean isFinal = ap.recordResponse(userId, confirmed);
                if (isFinal)
                    resolve(ap);
            }
        }
    }
}

class ActivePending {
    final UUID lobbyId;
    final PendingLobby pendingLobby;
    final Instant createdAt;
    private final Map<UserId, ConfirmationState> confirmations;
    private final AtomicBoolean resolving = new AtomicBoolean(false);

    private volatile ScheduledFuture<?> scheduledFuture;

    ActivePending(UUID lobbyId, PendingLobby pendingLobby, Instant createdAt) {
        this.lobbyId = lobbyId;
        this.pendingLobby = pendingLobby;
        this.createdAt = createdAt;
        this.confirmations = pendingLobby.members().stream()
                .map(LobbyMember::userView)
                .map(IUserView::id)
                .collect(java.util.stream.Collectors.toMap(id -> id, id -> ConfirmationState.PENDING));
    }

    synchronized boolean recordResponse(UserId userId, boolean confirmed) {
        ConfirmationState state = confirmations.get(userId);
        if (state != ConfirmationState.PENDING)
            return false;
        confirmations.put(userId, confirmed ? ConfirmationState.CONFIRMED : ConfirmationState.DECLINED);
        return isFinal();
    }

    synchronized void markTimeout() {
        for (var entry : confirmations.entrySet()) {
            if (entry.getValue() == ConfirmationState.PENDING) {
                entry.setValue(ConfirmationState.DECLINED);
            }
        }
    }

    synchronized Collection<LobbyMember> getAcceptedMembers() {
        return pendingLobby.members().stream()
                .filter(member -> confirmations.get(member.userView().id()) == ConfirmationState.CONFIRMED)
                .toList();
    }

    synchronized Collection<LobbyMember> getDeclinedMembers() {
        return pendingLobby.members().stream()
                .filter(member -> confirmations.get(member.userView().id()) == ConfirmationState.DECLINED)
                .toList();
    }

    synchronized boolean isFinal() {
        return confirmations.values().stream().noneMatch(state -> state == ConfirmationState.PENDING);
    }

    void cancelTimeout() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    boolean markResolving() {
        return resolving.compareAndSet(false, true);
    }

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    Lobby finalizeLobby() {
        if (!isFinal())
            throw new IllegalStateException("Cannot finalize lobby before all responses are in");
        for (var member : pendingLobby.members()) {
            member.userState().setState(UserState.State.IN_LOBBY);
        }
        return LobbyFactory.create(pendingLobby, lobbyId);
    }

    private enum ConfirmationState {
        PENDING,
        CONFIRMED,
        DECLINED
    }
}
