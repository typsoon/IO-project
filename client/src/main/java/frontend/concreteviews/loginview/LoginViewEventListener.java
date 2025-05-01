package frontend.concreteviews.loginview;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.socketwrappers.sendertypes.LoginStateSender;
import viewmodel.RequestHandler;

public class LoginViewEventListener implements EventListener {
  private final RequestHandler requestHandler;
  private final LoginStateSender loginStateSender;
  // TODO: maybe remove logging from here
  private final Logger logger = Logger.getLogger("LoginViewInputAdapter");

  public LoginViewEventListener(final RequestHandler requestHandler, final LoginStateSender loginStateSender) {
    this.requestHandler = requestHandler;
    this.loginStateSender = loginStateSender;
  }

  @Override
  public boolean handle(final Event event) {
    if (event instanceof final CredentialsTypedEvent credentialsTypedEvent) {
      // logger.info(() -> String.format("Typed in credentials: %s",
      // credentialsTypedEvent.getCredentials()));

      var credentials = credentialsTypedEvent.getCredentials();

      Optional<LogInResponse> result;
      try {
        result = this.loginStateSender.sendMessage(new LogInQuery(credentials.username(), credentials.password()));
      } catch (IOException e) {
        logger.info(String.format("Error occured wile sending message %s", e));
        result = Optional.empty();
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
      // requestHandler.handleRequest(new MoveToConfigurationRequest());

      return true;
    }
    return false;
  }
}
