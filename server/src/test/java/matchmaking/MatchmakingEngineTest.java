package matchmaking;

import database.IDatabaseManager.UserId;
import matchmaking.lobby.Lobby;
import matchmaking.lobby.LobbyMember;
import matchmaking.lobby.PendingLobby;
import matchmaking.pool.IMatchmakingPool;
import matchmaking.pool.IMatchmakingPoolFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.UserState;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchmakingEngineTest {

    private MatchmakingEngine engine;
    private IMatchmakingPoolFactory poolFactory;
    private IMatchmakingPool pool;
    private Consumer<PendingLobby> pendingLobbyConsumer;
    private LobbyMember member;
    private UserId userId;
    private MatchmakingParameters params;
    private UserState userState;

    @BeforeEach
    void setUp() {
        poolFactory = mock(IMatchmakingPoolFactory.class);
        pool = mock(IMatchmakingPool.class);
        pendingLobbyConsumer = mock(Consumer.class);

        params = new MatchmakingParameters(2);
        userId = new UserId(1);

        userState = mock(UserState.class);
        var userView = mock(gameclient.user.IUserView.class);
        when(userView.id()).thenReturn(userId);

        member = mock(LobbyMember.class);
        when(member.userView()).thenReturn(userView);
        when(member.userState()).thenReturn(userState);

        when(poolFactory.createPool(params)).thenReturn(pool);

        engine = new MatchmakingEngine(pendingLobbyConsumer, poolFactory);
    }

    @Test
    void findGame_addsUserToPoolAndSetsState() {
        when(pool.tryFormLobby()).thenReturn(Optional.empty());

        engine.findGame(member, params);

        verify(pool, times(1)).add(member);
        verify(userState, times(1)).setState(UserState.State.SEARCHING);
        assertTrue(engine.interruptSearch(userId));
    }

    @Test
    void findGame_formsLobbyAndCallsConsumer() {
        PendingLobby pendingLobby = new PendingLobby(List.of(member), params);
        when(pool.tryFormLobby()).thenReturn(Optional.of(pendingLobby));

        engine.findGame(member, params);

        verify(pendingLobbyConsumer, times(1)).accept(pendingLobby);
    }

    @Test
    void createGame_callsConsumerWithPendingLobby() {
        var members = List.of(member);
        engine.createGame(members, params);

        verify(pendingLobbyConsumer, times(1)).accept(argThat(lobby ->
                lobby.members().equals(members) && lobby.matchmakingParameters().equals(params)));
    }

    @Test
    void interruptSearch_removesUserFromAllPools() {
        when(pool.tryFormLobby()).thenReturn(Optional.empty());
        engine.findGame(member, params);

        when(pool.remove(userId)).thenReturn(true);

        boolean result = engine.interruptSearch(userId);

        verify(pool, times(1)).remove(userId);
        assertTrue(result);
    }

    @Test
    void interruptSearch_returnsFalseIfUserNotPresent() {
        boolean result = engine.interruptSearch(new UserId(99));
        assertFalse(result);
    }

    @Test
    void finalizeLobby_addsLobbyToList() {
        Lobby lobby = mock(Lobby.class);
        engine.finalizeLobby(lobby);

        // Dostęp do prywatnego pola przez refleksję (tylko do testów) (dont kill me)
        var lobbiesField = Arrays.stream(engine.getClass().getDeclaredFields())
                .filter(f -> f.getName().equals("lobbies"))
                .findFirst().orElseThrow();
        lobbiesField.setAccessible(true);
        List<Lobby> lobbies = null;
        try {
            lobbies = (List<Lobby>) lobbiesField.get(engine);
        }
        catch (IllegalAccessException e) {
            fail("Reflection error");
        }
        assertTrue(lobbies.contains(lobby));
    }
}