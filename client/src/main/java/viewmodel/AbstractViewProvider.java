package viewmodel;

import network.ClientSocketWrapper;

public interface AbstractViewProvider {
  AbstractView createConfigurationView(RequestHandler requestHandler, ClientSocketWrapper clientSocketWrapper);

  AbstractView createGameplayView(RequestHandler requestHandler, ClientSocketWrapper clientSocketWrapper);

  AbstractView createGameplayStatsView(RequestHandler requestHandler, ClientSocketWrapper clientSocketWrapper);
}
