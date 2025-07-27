package frontend.concreteviews.loginview;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapper.ConnectionEndedException;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import viewmodel.AbstractViewManager;

public class LoginViewEventListener implements EventListener {
    private final AbstractViewManager viewManager;
    // TODO: maybe remove logging from here
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final Logger logger = Logger.getLogger("LoginViewInputAdapter");

    public LoginViewEventListener(final AbstractViewManager viewManager,
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

            Optional<LogInResponse> result;
            try {
                result = this.clientSideSocketWrapper.dispatchMessage(
                        new LogInQuery(credentials.login(), credentials.password()));
            } catch (IOException e) {
                logger.info(String.format("Error occured wile sending message %s", e));
                result = Optional.empty();
            } catch (ConnectionEndedException connectionEndedException) {
                // FIXME: Security issue: doing this here can grow stack infinitely and we don't
                // want that
                viewManager.moveToMainMenu();
                return true;
            }

            if (result.isEmpty()) {
                throw new IllegalStateException("There should be a response to LogInQuery");
            }
            var response_payload = result.get().authTokenOptional();

            if (response_payload.isEmpty()) {
                logger.info("Invalid credentials");
                return false;
            }

            logger.info("Succesfully logged in");
            viewManager.moveToGameClient(clientSideSocketWrapper);
            // var clientSideSocketWrapper =
            // clientSideSocketWrapperFactory.getClientSideSocketWrapper();

            // viewManager.moveToGameClient(clientSideSocketWrapper);
            // requestHandler.handleRequest(new MoveToConfigurationRequest());

            return true;
        }
        return false;
    }
}
