package matchmaking.lobby;

import game.engine.PlayerConfig;
import game.session.PlayerData;
import game.session.ISubscribablePlayerConnector;
import org.junit.jupiter.api.Test;
import user.IMatchmakingUserHandle;
import matchmaking.MatchmakingParameters;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LobbyFactoryTest {

    @Test
    void create_createsLobbyWithCorrectMembersAndId() {
        IMatchmakingUserHandle userHandle = mock(IMatchmakingUserHandle.class);
        PlayerData playerData = mock(PlayerData.class);
        PlayerConfig playerConfig = mock(PlayerConfig.class);
        when(playerData.config()).thenReturn(playerConfig);
        when(userHandle.getPlayerData()).thenReturn(playerData);
        when(playerData.connector()).thenReturn(mock(ISubscribablePlayerConnector.class));

        LobbyMember member = new LobbyMember(
                mock(gameclient.user.IUserView.class),
                userHandle,
                mock(user.UserState.class)
        );
        PendingLobby pendingLobby = new PendingLobby(List.of(member), mock(MatchmakingParameters.class));
        UUID lobbyId = UUID.randomUUID();

        Lobby lobby = LobbyFactory.create(pendingLobby, lobbyId);

        assertNotNull(lobby);
        assertEquals(1, lobby.getPlayerData().size());
        assertEquals(playerData, lobby.getPlayerData().iterator().next());
    }

    @Test
    void create_setsGameStartedCallbackForEachMember() {
        IMatchmakingUserHandle userHandle = mock(IMatchmakingUserHandle.class);
        PlayerData playerData = mock(PlayerData.class);
        PlayerConfig playerConfig = mock(PlayerConfig.class);
        ISubscribablePlayerConnector connector = mock(ISubscribablePlayerConnector.class);
        when(playerData.config()).thenReturn(playerConfig);
        when(userHandle.getPlayerData()).thenReturn(playerData);
        when(playerData.connector()).thenReturn(connector);

        LobbyMember member = new LobbyMember(
                mock(gameclient.user.IUserView.class),
                userHandle,
                mock(user.UserState.class)
        );
        PendingLobby pendingLobby = new PendingLobby(List.of(member), mock(MatchmakingParameters.class));
        UUID lobbyId = UUID.randomUUID();

        LobbyFactory.create(pendingLobby, lobbyId);

        verify(userHandle, times(1)).gameStarted(any());
    }
}