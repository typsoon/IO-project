package frontend.concreteviews.loginview;

import java.io.IOException;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import network.client.ClientSideSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.loginstate.LogInQuery;
import viewmodel.IViewManager;

public class LoginViewEventListener implements EventListener {
    private final IViewManager viewManager;
    // TODO: maybe remove logging from here
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final Logger logger = Logger.getLogger("LoginViewInputAdapter");

    public LoginViewEventListener(final IViewManager viewManager,
            ClientSideSocketWrapper clientSideSocketWrapper) {
        this.viewManager = viewManager;
        this.clientSideSocketWrapper = clientSideSocketWrapper;
    }

    @Override
    public boolean handle(final Event event) {
        if (event instanceof final CredentialsTypedEvent credentialsTypedEvent) {
            // logger.info(() -> String.format("Typed in credentials: %s",
            // credentialsTypedEvent.getCredentials()));

            var credentials = credentialsTypedEvent.getCredentials();

            try {
                this.clientSideSocketWrapper.dispatchMessage(
                        new LogInQuery(credentials.login(), credentials.password()));
            } catch (IOException e) {
                logger.info(String.format("Error occured while sending message %s", e));
            } catch (ConnectionEndedException connectionEndedException) {
                // FIXME: Security issue: doing this here can grow stack infinitely and we don't
                // want that
                viewManager.moveToMainMenu();
                return true;
            }

            return true;
        }
        return false;
    }
}
