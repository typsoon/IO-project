package frontend.concreteviewproviders;

import com.badlogic.gdx.Game;

import network.ClientSocketWrapper;
import viewmodel.AbstractView;
import viewmodel.AbstractViewProvider;
import viewmodel.RequestHandler;

public class AdminViewProvider implements AbstractViewProvider {
  private final Game game;

  public AdminViewProvider(Game game) {
    this.game = game;
  }

  @Override
  public AbstractView createConfigurationView(RequestHandler requestHandler, ClientSocketWrapper clientSocketWrapper) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createConfigurationView'");
  }

  @Override
  public AbstractView createGameplayView(RequestHandler requestHandler, ClientSocketWrapper clientSocketWrapper) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createGameplayView'");
  }

  @Override
  public AbstractView createGameplayStatsView(RequestHandler requestHandler, ClientSocketWrapper clientSocketWrapper) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createGameplayStatsView'");
  }
}
