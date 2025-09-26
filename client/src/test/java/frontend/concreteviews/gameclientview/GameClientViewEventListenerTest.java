package frontend.concreteviews.gameclientview;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import matchmaking.MatchmakingParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import frontend.concreteviews.gameclientview.GameClientViewEvents.ConfirmGameEvent;
import frontend.concreteviews.gameclientview.GameClientViewEvents.CreateRoomEvent;
import frontend.concreteviews.gameclientview.GameClientViewEvents.FindGameEvent;
import frontend.concreteviews.gameclientview.GameClientViewEvents.RequestGameStartEvent;
import gameclient.rooms.RoomConfig;
import network.client.ClientSideSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.configurationstate.GameStartMessages.StartGameRequest;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.userstate.GameConfirmation;
import utils.ISendable;
import viewmodel.IViewManager;

@ExtendWith(MockitoExtension.class)
class GameClientViewEventListenerTest {

    @Mock
    private IViewManager viewManager;

    @Mock
    private ClientSideSocketWrapper socket;

    @Mock
    private ObjectToMessageDecoder decoder;

    private GameClientViewEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new GameClientViewEventListener(viewManager, socket, decoder);
    }

    @Test
    void handle_CreateRoomEvent_DispatchesMessage() throws Exception {
        CreateRoomEvent ev = mock(CreateRoomEvent.class);
        when(ev.getName()).thenReturn("room-1");
        when(ev.getPassword()).thenReturn("secret");
        when(ev.getMaxPlayers()).thenReturn(4);
        when(ev.isPublic()).thenReturn(true);

        when(decoder.decodeFromRecord(any())).thenReturn(null);

        boolean result = listener.handle(ev);

        assertTrue(result);
        ArgumentCaptor<Object> payloadCap = ArgumentCaptor.forClass(Object.class);
        verify(decoder, times(1)).decodeFromRecord((ISendable) payloadCap.capture());
        verify(socket, times(1)).dispatchMessage(any());

        Object payload = payloadCap.getValue();
        assertNotNull(payload);
        assertInstanceOf(RoomConfig.class, payload);
        verify(ev, atLeastOnce()).getName();
        verify(ev, atLeastOnce()).getPassword();
        verify(ev, atLeastOnce()).getMaxPlayers();
        verify(ev, atLeastOnce()).isPublic();
    }

    @Test
    void handle_RequestGameStartEvent_DispatchesMessage() throws Exception {
        RequestGameStartEvent ev = mock(RequestGameStartEvent.class);
        when(decoder.decodeFromRecord(any())).thenReturn(null);

        boolean result = listener.handle(ev);

        assertTrue(result);
        ArgumentCaptor<Object> payloadCap = ArgumentCaptor.forClass(Object.class);
        verify(decoder, times(1)).decodeFromRecord((ISendable) payloadCap.capture());
        verify(socket, times(1)).dispatchMessage(any());

        Object payload = payloadCap.getValue();
        assertNotNull(payload);
        assertInstanceOf(StartGameRequest.Payload.class, payload);
    }

    @Test
    void handle_ConfirmGameEvent_DispatchesMessage() throws Exception {
        ConfirmGameEvent ev = mock(ConfirmGameEvent.class);
        when(ev.getConfirmationVal()).thenReturn(GameConfirmation.Confirmation.CONFIRMED);
        when(decoder.decodeFromRecord(any())).thenReturn(null);

        boolean result = listener.handle(ev);

        assertTrue(result);
        ArgumentCaptor<Object> payloadCap = ArgumentCaptor.forClass(Object.class);
        verify(decoder, times(1)).decodeFromRecord((ISendable) payloadCap.capture());
        verify(socket, times(1)).dispatchMessage(any());
        // było: times(1) — implementacja woła 2x (konstrukcja + log)
        verify(ev, atLeastOnce()).getConfirmationVal();

        Object payload = payloadCap.getValue();
        assertNotNull(payload);
        assertInstanceOf(GameConfirmation.class, payload);
    }

    @Test
    void handle_FindGameEvent_DispatchesMessage_UsingProvidedParams() throws Exception {
        FindGameEvent ev = mock(FindGameEvent.class);
        MatchmakingParameters matchmakingParams = mock(MatchmakingParameters.class);
        when(ev.getMatchmakingParameters()).thenReturn(matchmakingParams);
        when(decoder.decodeFromRecord(any())).thenReturn(null);

        boolean result = listener.handle(ev);

        assertTrue(result);
        ArgumentCaptor<Object> payloadCap = ArgumentCaptor.forClass(Object.class);
        verify(decoder, times(1)).decodeFromRecord((ISendable) payloadCap.capture());
        verify(socket, times(1)).dispatchMessage(any());
        verify(ev, times(1)).getMatchmakingParameters();

        Object payload = payloadCap.getValue();
        assertSame(matchmakingParams, payload);
    }

    @Test
    void handle_UnknownEvent_ReturnsFalse_AndDoesNotDispatch() throws Exception {
        com.badlogic.gdx.scenes.scene2d.Event unknown = new com.badlogic.gdx.scenes.scene2d.Event();

        boolean result = listener.handle(unknown);

        assertFalse(result);
        verifyNoInteractions(decoder);
        verifyNoInteractions(socket);
    }

    @Test
    void handle_DispatchIOException_ReturnsFalse() throws Exception {
        RequestGameStartEvent ev = mock(RequestGameStartEvent.class);
        when(decoder.decodeFromRecord(any())).thenReturn(null);
        doThrow(new IOException("net down")).when(socket).dispatchMessage(any());

        boolean result = listener.handle(ev);

        assertFalse(result);
        verify(socket, times(1)).dispatchMessage(any());
        verifyNoInteractions(viewManager);
    }

    @Test
    void handle_DispatchConnectionEnded_MovesToMainMenu_ReturnsTrue() throws Exception {
        RequestGameStartEvent ev = mock(RequestGameStartEvent.class);
        when(decoder.decodeFromRecord(any())).thenReturn(null);
        doThrow(new ConnectionEndedException()).when(socket).dispatchMessage(any());

        boolean result = listener.handle(ev);

        assertTrue(result);
        verify(viewManager, times(1)).moveToMainMenu();
    }
}
