package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;

import frontend.ViewWithEventLoop;
import network.client.ClientSideSocketWrapper;
import viewmodel.IViewManager;
import viewmodel.IView;

public class LoginViewFactory {
    private final Game game;

    public LoginViewFactory(final Game game) {
        this.game = game;
    }

    public IView getLoginView(IViewManager viewManager, ClientSideSocketWrapper clientSideSocketWrapper) {
        var loginView = new LoginView(
                game,
                new LoginViewEventListener(viewManager, clientSideSocketWrapper),
                viewManager.getTextureManager(),
                viewManager);

        var handler = new LoginViewMessageHandler(clientSideSocketWrapper, viewManager);

        return new ViewWithEventLoop(handler, loginView,
                game);
    }
}
