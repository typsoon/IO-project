package frontend.concreteviews.gameclientview;

import java.io.IOException;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.concreteviews.gameclientview.GameClientViewEvents.ConfirmGameEvent;
import frontend.concreteviews.gameclientview.GameClientViewEvents.CreateRoomEvent;
import frontend.concreteviews.gameclientview.GameClientViewEvents.RequestGameStartEvent;
import frontend.concreteviews.gameclientview.GameClientViewEvents.FindGameEvent;
import gameclient.rooms.RoomConfig;
import network.client.ClientSideSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.configurationstate.GameStartMessages.*;
import network.messages.configurationstate.RoomMessages;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.userstate.GameConfirmation;
import viewmodel.IViewManager;

public class GameClientViewEventListener implements EventListener {
    private final IViewManager viewManager;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final ObjectToMessageDecoder objectToMessageDecoder;
    private final Logger logger = Logger.getGlobal();

    public GameClientViewEventListener(IViewManager viewManager,
                                       ClientSideSocketWrapper clientSideSocketWrapper, ObjectToMessageDecoder objectToMessageDecoder) {
        this.viewManager = viewManager;
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.objectToMessageDecoder = objectToMessageDecoder;
    }

    @Override
    public boolean handle(Event event) {
        try {
            switch (event) {
                case CreateRoomEvent createRoomEvent -> {
                    var msgPayload = new RoomConfig(createRoomEvent.getName(), createRoomEvent.getPassword(),
                            createRoomEvent.getMaxPlayers(), createRoomEvent.isPublic());
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Sent create room request");
                    clientSideSocketWrapper.dispatchMessage(msg);
                    return true;
                }

                case GameClientViewEvents.JoinRoomEvent joinRoomEvent -> {
                    var msgPayload = new RoomMessages.JoinRoomRequest.Payload(joinRoomEvent.getRoomName(), joinRoomEvent.getPassword());
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Sent join room request");
                    clientSideSocketWrapper.dispatchMessage(msg);
                    return true;
                }

                case GameClientViewEvents.BrowseRoomsEvent browseRoomsEvent -> {
                    var msgPayload = new RoomMessages.BrowseRoomsRequest.Payload();
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Sent browse rooms request");
                    clientSideSocketWrapper.dispatchMessage(msg);
                    return true;
                }

                case RequestGameStartEvent requestGameStartEvent -> {
                    var msgPayload = new StartGameRequest.Payload();
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Send game start request");
                    clientSideSocketWrapper.dispatchMessage(msg);
                    return true;
                }

                case ConfirmGameEvent confirmGameEvent -> {
                    var msgPayload = new GameConfirmation(confirmGameEvent.getConfirmationVal());
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Confirmed game %s".formatted(confirmGameEvent.getConfirmationVal()));
                    clientSideSocketWrapper.dispatchMessage(msg);
                    return true;
                }

                case FindGameEvent findGameEvent -> {
                    var msgPayload = findGameEvent.getMatchmakingParameters();
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Sent find game request");
                    clientSideSocketWrapper.dispatchMessage(msg);
                    return true;
                }

                default -> {
                }
            }
        }
        catch (IOException e) {
            logger.info(String.format("Error occured while sending message %s", e));
        }
        catch (ConnectionEndedException connectionEndedException) {
            // FIXME: Security issue: doing this here can grow stack infinitely and we don't
            // want that

            viewManager.moveToMainMenu();
            return true;
        }
        return false;
    }
}
