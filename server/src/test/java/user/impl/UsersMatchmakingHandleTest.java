package user.impl;

import matchmaking.IMatchmakingEngine;
import matchmaking.MatchmakingParameters;
import matchmaking.lobby.LobbyMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import user.IUsersMatchmakingHandle;
import user.UserState;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersMatchmakingHandleTest {

    private IMatchmakingEngine matchmakingEngine;
    private LobbyMember member;
    private UserState userState;
    private UsersMatchmakingHandle handle;

    @BeforeEach
    void setUp() {
        matchmakingEngine = mock(IMatchmakingEngine.class);
        userState = new UserState();
        member = mock(LobbyMember.class);
        when(member.userState()).thenReturn(userState);
        when(member.userView()).thenReturn(mock(gameclient.user.IUserView.class));
        handle = new UsersMatchmakingHandle(matchmakingEngine, member);
    }

    @Test
    void findGame_returnsAlreadyInRoom_whenUserInRoom() {
        userState.setState(UserState.State.IN_ROOM);
        MatchmakingParameters params = mock(MatchmakingParameters.class);

        var result = handle.findGame(params);

        assertEquals(IUsersMatchmakingHandle.JoinGameRequestResult.ALREADY_IN_ROOM, result);
        verify(matchmakingEngine, never()).findGame(any(), any());
    }

    @Test
    void findGame_callsEngineAndReturnsSuccess_whenUserNotInRoom() {
        userState.setState(UserState.State.DEFAULT);
        MatchmakingParameters params = mock(MatchmakingParameters.class);

        var result = handle.findGame(params);

        assertEquals(IUsersMatchmakingHandle.JoinGameRequestResult.REQUEST_SUCCESSFUL, result);
        verify(matchmakingEngine, times(1)).findGame(member, params);
    }

    @Test
    void interruptGameLookup_delegatesToEngine() {
        var userView = mock(gameclient.user.IUserView.class);
        when(member.userView()).thenReturn(userView);
        var userId = mock(database.IDatabaseManager.UserId.class);
        when(userView.id()).thenReturn(userId);
        when(matchmakingEngine.interruptSearch(userId)).thenReturn(true);

        boolean result = handle.interruptGameLookup();

        assertTrue(result);
        verify(matchmakingEngine, times(1)).interruptSearch(userId);
    }
}