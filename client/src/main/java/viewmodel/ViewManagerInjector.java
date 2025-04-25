package viewmodel;

import com.badlogic.gdx.Game;

import frontend.concreteviews.loginview.LoginViewFactory;
import frontend.concreteviewproviders.AdminViewProvider;
import frontend.concreteviewproviders.UserViewProvider;

public class ViewManagerInjector {
  private final Game game;

  public ViewManagerInjector(Game game) {
    this.game = game;
  }

  public ViewManager getViewManager() {
    var loginViewFactory = new LoginViewFactory(game);
    var userViewProvider = new UserViewProvider(game);
    var adminViewProvider = new AdminViewProvider(game);

    return new ViewManager(userViewProvider, adminViewProvider, loginViewFactory);
  }
}
