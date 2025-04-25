package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;

import viewmodel.AbstractLoginViewFactory;
import viewmodel.AbstractView;
import viewmodel.RequestHandler;

public class LoginViewFactory implements AbstractLoginViewFactory {
  private final Game game;

  public LoginViewFactory(final Game game) {
    this.game = game;
  }

  public AbstractView getLoginView(final RequestHandler requestHandler) {
    final var loginViewInputAdapter = new LoginViewInputAdapter(requestHandler);
    return new LoginView(game, loginViewInputAdapter);
  }

}
