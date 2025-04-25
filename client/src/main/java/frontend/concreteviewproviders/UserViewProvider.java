package frontend.concreteviewproviders;

import com.badlogic.gdx.Game;

import network.ClientSocketWrapper;
import viewmodel.AbstractView;
import viewmodel.AbstractViewProvider;
import viewmodel.RequestHandler;

public class UserViewProvider implements AbstractViewProvider {
  private final Game game;

  public UserViewProvider(final Game game) {
    this.game = game;
  }

  @Override
  public AbstractView createConfigurationView(final RequestHandler requestHandler,
      final ClientSocketWrapper clientSocketWrapper) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createConfigurationView'");
  }

  @Override
  public AbstractView createGameplayView(final RequestHandler requestHandler,
      final ClientSocketWrapper clientSocketWrapper) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createGameplayView'");
  }

  @Override
  public AbstractView createGameplayStatsView(final RequestHandler requestHandler,
      final ClientSocketWrapper clientSocketWrapper) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createGameplayStatsView'");
  }
}
