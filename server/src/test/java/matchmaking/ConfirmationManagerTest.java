package matchmaking;

import game.engine.PlayerConfig;
import game.engine.entities.geometry.GeometryConfigID;
import game.session.ISubscribablePlayerConnector;
import game.session.PlayerData;
import matchmaking.lobby.Lobby;
import matchmaking.lobby.LobbyMember;
import matchmaking.lobby.PendingLobby;
import network.messages.userstate.GameConfirmation;
import user.UserState;
import gameclient.user.IUserView;
import database.IDatabaseManager.UserId;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfirmationManagerTest {

    private ConfirmationManager manager;
    private Consumer<Lobby> lobbyFinalized;
    private BiConsumer<Collection<LobbyMember>, MatchmakingParameters> requeueHandler;
    private LobbyMember member1, member2;
    private UserState state1, state2;
    private IUserView userView1, userView2;
    private UserId userId1, userId2;
    private MatchmakingParameters params;
    private PendingLobby pendingLobby;

    @BeforeEach
    void setUp() {
        lobbyFinalized = mock(Consumer.class);
        requeueHandler = mock(BiConsumer.class);
        manager = new ConfirmationManager(lobbyFinalized, requeueHandler, 100);

        userId1 = new UserId(1);
        userId2 = new UserId(2);

        state1 = mock(UserState.class);
        state2 = mock(UserState.class);

        userView1 = mock(IUserView.class);
        userView2 = mock(IUserView.class);
        when(userView1.id()).thenReturn(userId1);
        when(userView2.id()).thenReturn(userId2);

        member1 = mock(LobbyMember.class);
        member2 = mock(LobbyMember.class);
        when(member1.userView()).thenReturn(userView1);
        when(member2.userView()).thenReturn(userView2);
        when(member1.userState()).thenReturn(state1);
        when(member2.userState()).thenReturn(state2);

        var handle1 = mock(user.IMatchmakingUserHandle.class);
        var handle2 = mock(user.IMatchmakingUserHandle.class);

        var playerConfig1 = new PlayerConfig();
        var playerConfig2 = new PlayerConfig();

        ISubscribablePlayerConnector connector1 = mock(ISubscribablePlayerConnector.class);
        ISubscribablePlayerConnector connector2 = mock(ISubscribablePlayerConnector.class);

        var playerData1 = new PlayerData(connector1, playerConfig1);
        var playerData2 = new PlayerData(connector2, playerConfig2);

        when(handle1.getPlayerData()).thenReturn(playerData1);
        when(handle2.getPlayerData()).thenReturn(playerData2);
        when(handle1.getPlayerConnector()).thenReturn(connector1);
        when(handle2.getPlayerConnector()).thenReturn(connector2);

        when(handle1.getPlayerConfig()).thenReturn(playerConfig1);
        when(handle2.getPlayerConfig()).thenReturn(playerConfig2);

        when(member1.matchmakingUserHandle()).thenReturn(handle1);
        when(member2.matchmakingUserHandle()).thenReturn(handle2);

        params = new MatchmakingParameters(2);
        pendingLobby = new PendingLobby(List.of(member1, member2), params);
    }

    @Test
    void accept_setsStateAndSchedulesTimeout() throws Exception {
        manager.accept(pendingLobby);

        verify(state1).setState(UserState.State.MATCHED_PENDING_CONFIRM);
        verify(state2).setState(UserState.State.MATCHED_PENDING_CONFIRM);

        TimeUnit.MILLISECONDS.sleep(150);
        verify(requeueHandler, atLeastOnce()).accept(any(), eq(params));
    }

    @Test
    void confirmationConsumer_confirmsAndFinalizesLobby() {
        manager.accept(pendingLobby);

        var activePendingsField = Arrays.stream(manager.getClass().getDeclaredFields())
                .filter(f -> f.getName().equals("activePendings"))
                .findFirst().orElseThrow();
        activePendingsField.setAccessible(true);
        Map<UUID, ?> activePendings = null;
        try {
            activePendings = (Map<UUID, ?>) activePendingsField.get(manager);
        }
        catch (IllegalAccessException e) {
            fail("Reflection error");
        }
        UUID lobbyId = activePendings.keySet().iterator().next();

        var consumer1 = manager.new ConfirmationConsumer(userId1, lobbyId);
        var consumer2 = manager.new ConfirmationConsumer(userId2, lobbyId);

        consumer1.processSendable(new GameConfirmation(GameConfirmation.Confirmation.CONFIRMED));
        consumer2.processSendable(new GameConfirmation(GameConfirmation.Confirmation.CONFIRMED));

        verify(lobbyFinalized, times(1)).accept(any(Lobby.class));
    }

    @Test
    void confirmationConsumer_declinesAndTriggersRequeue() {
        manager.accept(pendingLobby);

        var activePendingsField = Arrays.stream(manager.getClass().getDeclaredFields())
                .filter(f -> f.getName().equals("activePendings"))
                .findFirst().orElseThrow();
        activePendingsField.setAccessible(true);
        Map<UUID, ?> activePendings = null;
        try {
            activePendings = (Map<UUID, ?>) activePendingsField.get(manager);
        }
        catch (IllegalAccessException e) {
            fail("Reflection error");
        }
        UUID lobbyId = activePendings.keySet().iterator().next();

        var consumer1 = manager.new ConfirmationConsumer(userId1, lobbyId);
        var consumer2 = manager.new ConfirmationConsumer(userId2, lobbyId);

        consumer1.processSendable(new GameConfirmation(GameConfirmation.Confirmation.CONFIRMED));
        consumer2.processSendable(new GameConfirmation(GameConfirmation.Confirmation.CANCELLED));

        verify(requeueHandler, times(1)).accept(any(), eq(params));
    }

    @Test
    void activePending_recordResponse_and_isFinal() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        assertFalse(ap.isFinal());
        assertFalse(ap.recordResponse(userId1, true));
        assertFalse(ap.isFinal());
        assertTrue(ap.recordResponse(userId2, false));
        assertTrue(ap.isFinal());
    }

    @Test
    void activePending_markTimeout_setsDeclinedForPending() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        ap.recordResponse(userId1, true);
        ap.markTimeout();
        var declined = ap.getDeclinedMembers();
        assertEquals(1, declined.size());
        assertEquals(member2, declined.iterator().next());
    }

    @Test
    void activePending_getAcceptedAndDeclinedMembers() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        ap.recordResponse(userId1, true);
        ap.recordResponse(userId2, false);

        var accepted = ap.getAcceptedMembers();
        var declined = ap.getDeclinedMembers();

        assertEquals(1, accepted.size());
        assertEquals(member1, accepted.iterator().next());
        assertEquals(1, declined.size());
        assertEquals(member2, declined.iterator().next());
    }

    @Test
    void activePending_finalizeLobby_setsStateAndReturnsLobby() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        ap.recordResponse(userId1, true);
        ap.recordResponse(userId2, true);

        Lobby lobby = ap.finalizeLobby();
        assertNotNull(lobby);
        verify(state1).setState(UserState.State.IN_LOBBY);
        verify(state2).setState(UserState.State.IN_LOBBY);
    }

    @Test
    void activePending_finalizeLobby_throwsIfNotFinal() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        ap.recordResponse(userId1, true);
        assertThrows(IllegalStateException.class, ap::finalizeLobby);
    }

    @Test
    void activePending_cancelTimeout_cancelsScheduledFuture() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        var future = mock(java.util.concurrent.ScheduledFuture.class);
        ap.setScheduledFuture(future);
        ap.cancelTimeout();
        verify(future).cancel(false);
    }

    @Test
    void activePending_markResolving_onlyOnce() {
        var ap = new ActivePending(UUID.randomUUID(), pendingLobby, java.time.Instant.now());
        assertTrue(ap.markResolving());
        assertFalse(ap.markResolving());
    }
}