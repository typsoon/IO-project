package viewmodel;

import viewmodel.requests.AbstractRequest;
import viewmodel.requests.MoveToConfigurationRequest;

public class ViewManager implements RequestHandler {
  private final AbstractViewProvider userViewProvider;
  private final AbstractViewProvider adminViewProvider;
  private final AbstractLoginViewFactory loginViewFactory;

  public ViewManager(final AbstractViewProvider userViewProvider,
      final AbstractViewProvider adminViewProvider,
      final AbstractLoginViewFactory abstractLoginViewFactory) {
    this.userViewProvider = userViewProvider;
    this.adminViewProvider = adminViewProvider;
    this.loginViewFactory = abstractLoginViewFactory;
  }

  public void start() {
    final var loginView = loginViewFactory.getLoginView(this);
    loginView.display();
  }

  @Override
  public void handleRequest(final AbstractRequest<?> request) {
    switch (request) {
      case final MoveToConfigurationRequest moveToConfigurationRequest -> {
        final var clientSocketWrapper = moveToConfigurationRequest.getPayload();
        final var configurationView = userViewProvider.createConfigurationView(this, clientSocketWrapper);
        configurationView.display();
      }
      default -> {
        throw new IllegalArgumentException(request.toString());
      }
    }
  };
}
