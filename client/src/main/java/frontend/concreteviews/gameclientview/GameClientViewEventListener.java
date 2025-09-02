package frontend.concreteviews.gameclientview;

import java.io.IOException;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.concreteviews.gameclientview.GameClientViewEvents.CreateRoomEvent;
import gameclient.rooms.RoomConfig;
import network.client.ClientSideSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.defaultmessage.ObjectToMessageDecoder;
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

                    // TODO: change this so it has all the info
                    var msgPayload = new RoomConfig(createRoomEvent.getName(), "", 1, true);
                    var msg = objectToMessageDecoder.decodeFromRecord(msgPayload);

                    logger.info("Sent create room request");
                    clientSideSocketWrapper.dispatchMessage(msg);
                }

                default -> {
                }
            }
        } catch (IOException e) {
            logger.info(String.format("Error occured while sending message %s", e));
        } catch (ConnectionEndedException connectionEndedException) {
            // FIXME: Security issue: doing this here can grow stack infinitely and we don't
            // want that

            viewManager.moveToMainMenu();
            return true;
        }
        return false;
    }
}
