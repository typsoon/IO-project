package frontend.concreteviews.serverlookupview;

import com.badlogic.gdx.Game;

import viewmodel.AbstractServerLookupViewFactory;
import viewmodel.AbstractView;
import viewmodel.RequestHandler;

public class ServerLookupViewFactory implements AbstractServerLookupViewFactory {
  private final Game game;

  public ServerLookupViewFactory(final Game game) {
    this.game = game;
  }

  @Override
  public AbstractView getServerLookupView(RequestHandler requestHandler) {
    final var loginViewInputAdapter = new ServerLookupViewEventListener(requestHandler);
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getServerLookupView'");
  }
}
