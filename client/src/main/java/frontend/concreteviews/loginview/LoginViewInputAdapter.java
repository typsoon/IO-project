package frontend.concreteviews.loginview;

import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import viewmodel.RequestHandler;

public class LoginViewInputAdapter implements EventListener {
  private final RequestHandler requestHandler;
  // TODO: maybe remove logging from here
  private final Logger logger = Logger.getLogger("LoginViewInputAdapter");

  public LoginViewInputAdapter(final RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  @Override
  public boolean handle(final Event event) {
    if (event instanceof final CredentialsTypedEvent credentialsTypedEvent) {
      logger.info(() -> String.format("Received credentials: %s", credentialsTypedEvent.getCredentials()));

      return true;
    }
    return false;
  }
}
