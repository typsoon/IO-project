package viewmodel;

import network.socketwrappers.SenderTypes.ConfigurationStateSender;

public interface AbstractViewProvider {
    AbstractLoginView createConfigurationView(RequestHandler requestHandler, ConfigurationStateSender configurationSocket);

    // AbstractLoginView createGameplayView(RequestHandler requestHandler,
    // ClientSocketWrapper clientSocketWrapper);
    //
    // AbstractLoginView createGameplayStatsView(RequestHandler requestHandler,
    // ClientSocketWrapper clientSocketWrapper);
}
