package matchmaking.pool;

import database.IDatabaseManager;
import matchmaking.MatchmakingParameters;
import matchmaking.lobby.LobbyMember;
import gameclient.user.IUserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicMatchmakingPoolTest {

    private BasicMatchmakingPool pool;
    private MatchmakingParameters params;

    @BeforeEach
    void setUp() {
        params = new MatchmakingParameters(2);
        pool = new BasicMatchmakingPool(params);
    }

    private LobbyMember mockMember(int id) {
        IUserView userView = mock(IUserView.class);
        when(userView.id()).thenReturn(new IDatabaseManager.UserId(id));
        return new LobbyMember(userView, null, null);
    }

    @Test
    void add_addsUserIfNotPresent() {
        LobbyMember member = mockMember(1);
        pool.add(member);
        assertEquals(1, pool.size());
        assertTrue(pool.contains(new IDatabaseManager.UserId(1)));
    }

    @Test
    void add_doesNotAddDuplicateUser() {
        LobbyMember member = mockMember(1);
        pool.add(member);
        pool.add(member);
        assertEquals(1, pool.size());
    }

    @Test
    void remove_removesUserIfPresent() {
        LobbyMember member = mockMember(1);
        pool.add(member);
        boolean removed = pool.remove(new IDatabaseManager.UserId(1));
        assertTrue(removed);
        assertEquals(0, pool.size());
        assertFalse(pool.contains(new IDatabaseManager.UserId(1)));
    }

    @Test
    void remove_returnsFalseIfUserNotPresent() {
        boolean removed = pool.remove(new IDatabaseManager.UserId(99));
        assertFalse(removed);
    }

    @Test
    void tryFormLobby_returnsEmptyIfNotEnoughUsers() {
        pool.add(mockMember(1));
        Optional<?> lobby = pool.tryFormLobby();
        assertTrue(lobby.isEmpty());
    }

    @Test
    void tryFormLobby_formsLobbyWhenEnoughUsers() {
        pool.add(mockMember(1));
        pool.add(mockMember(2));
        Optional<?> lobby = pool.tryFormLobby();
        assertTrue(lobby.isPresent());
        assertEquals(0, pool.size());
    }

    @Test
    void size_returnsCorrectSize() {
        assertEquals(0, pool.size());
        pool.add(mockMember(1));
        assertEquals(1, pool.size());
        pool.add(mockMember(2));
        assertEquals(2, pool.size());
    }

    @Test
    void contains_returnsTrueIfUserPresent() {
        pool.add(mockMember(1));
        assertTrue(pool.contains(new IDatabaseManager.UserId(1)));
    }

    @Test
    void contains_returnsFalseIfUserNotPresent() {
        assertFalse(pool.contains(new IDatabaseManager.UserId(42)));
    }
}