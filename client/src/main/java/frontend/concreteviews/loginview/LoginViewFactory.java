package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;

import network.socketwrappers.SenderTypes.LoginStateSender;
import viewmodel.AbstractDefaultViewManager;
import viewmodel.AbstractLoginViewFactory;
import viewmodel.AbstractView;
import viewmodel.RequestHandler;

public class LoginViewFactory implements AbstractLoginViewFactory {
    private final Game game;

    public LoginViewFactory(final Game game) {
        this.game = game;
    }
    //TODO view: refactor to make similar to MainMenuViewFactory
    public AbstractView getLoginView(final RequestHandler requestHandler, LoginStateSender authenticatingSocket, AbstractDefaultViewManager viewManager) {
        final var loginViewInputAdapter = new LoginViewEventListener(requestHandler, authenticatingSocket);
        return new LoginView(game, loginViewInputAdapter, viewManager);
    }
}
