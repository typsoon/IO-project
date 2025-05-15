package viewmodel;

import network.socketwrappers.sendertypes.ConfigurationStateSender;

public interface AbstractViewProvider {
    AbstractView createConfigurationView(RequestHandler requestHandler, ConfigurationStateSender configurationSocket);

    // AbstractView createGameplayView(RequestHandler requestHandler,
    // ClientSocketWrapper clientSocketWrapper);
    //
    // AbstractView createGameplayStatsView(RequestHandler requestHandler,
    // ClientSocketWrapper clientSocketWrapper);
}
