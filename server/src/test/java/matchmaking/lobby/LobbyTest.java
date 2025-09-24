package matchmaking.lobby;

import game.actions.IAction;
import game.session.IActionReceiver;
import game.session.ISubscribablePlayerConnector;
import game.session.PlayerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import user.IMatchmakingUserHandle;
import utils.ISendable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LobbyTest {

    private IActionReceiver sessionManager;
    private IMatchmakingUserHandle matchmakingUserHandle;
    private PlayerData playerData;
    private LobbyMember lobbyMember;
    private Lobby lobby;

    @BeforeEach
    void setUp() {
        sessionManager = mock(IActionReceiver.class);
        matchmakingUserHandle = mock(IMatchmakingUserHandle.class);
        playerData = mock(PlayerData.class);
        when(matchmakingUserHandle.getPlayerData()).thenReturn(playerData);

        lobbyMember = new LobbyMember(
                mock(gameclient.user.IUserView.class),
                matchmakingUserHandle,
                mock(user.UserState.class)
        );

        lobby = new Lobby(List.of(lobbyMember), sessionManager, UUID.randomUUID());
    }

    @Test
    void getPlayerData_returnsPlayerDataFromMembers() {
        Collection<PlayerData> result = lobby.getPlayerData();
        assertEquals(1, result.size());
        assertEquals(playerData, result.iterator().next());
    }

    @Test
    void processSendable_sendsActionToSessionManager() {
        ISubscribablePlayerConnector connector = mock(ISubscribablePlayerConnector.class);
        IAction action = mock(IAction.class);

        lobby.processSendable(connector, action);

        verify(sessionManager, times(1)).sendAction(connector, action);
    }

    @Test
    void processSendable_logsUnexpectedSendable() {
        ISubscribablePlayerConnector connector = mock(ISubscribablePlayerConnector.class);
        ISendable sendable = mock(ISendable.class);

        lobby.processSendable(connector, sendable);

        verify(sessionManager, never()).sendAction(any(), any());
    }
}