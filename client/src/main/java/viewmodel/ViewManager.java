package viewmodel;

import frontend.requests.AbstractRequest;
import frontend.requests.MoveToConfigurationRequest;

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
    var loginView = loginViewFactory.getLoginView(this);
    loginView.display();
  }

  @Override
  public void handleRequest(AbstractRequest<?> request) {
    switch (request) {
      case MoveToConfigurationRequest moveToConfigurationRequest -> {
        var clientSocketWrapper = moveToConfigurationRequest.getPayload();
        var configurationView = userViewProvider.createConfigurationView(this, clientSocketWrapper);
        configurationView.display();
      }
      default -> {
        throw new IllegalArgumentException(request.toString());
      }
    }
  };
}
