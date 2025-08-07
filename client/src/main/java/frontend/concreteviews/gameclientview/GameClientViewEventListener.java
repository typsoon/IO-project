package frontend.concreteviews.gameclientview;

import java.io.IOException;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.concreteviews.gameclientview.GameClientViewEvents.CreateRoomEvent;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapper.ConnectionEndedException;
import network.messages.configurationstate.CreateRoomRequest;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import viewmodel.AbstractViewManager;

public class GameClientViewEventListener implements EventListener {
    private final AbstractViewManager viewManager;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final ObjectToMessageDecoder objectToMessageDecoder;
    private final Logger logger = Logger.getGlobal();

    public GameClientViewEventListener(AbstractViewManager viewManager,
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
                    var msgPayload = new CreateRoomRequest.Payload(createRoomEvent.getName());
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
