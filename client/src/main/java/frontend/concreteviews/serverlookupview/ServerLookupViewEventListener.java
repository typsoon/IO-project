package frontend.concreteviews.serverlookupview;

import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import viewmodel.RequestHandler;

public class ServerLookupViewEventListener implements EventListener {
    private final RequestHandler requestHandler;
    // TODO: maybe remove logging from here
    private final Logger logger = Logger.getLogger("LoginViewInputAdapter");

    public ServerLookupViewEventListener(final RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public boolean handle(final Event event) {
        if (event instanceof final ServerDataTypedEvent serverDataTypedEvent) {
            logger.info(() -> String.format("Received server data: %s", serverDataTypedEvent.getServerData()));

            return true;
        }
        return false;
    }
}
