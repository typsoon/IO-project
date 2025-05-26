package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;

import network.socketwrappers.SenderTypes.LoginStateSender;
import viewmodel.AbstractViewManager;
import viewmodel.AbstractView;
import viewmodel.RequestHandler;

            //TODO view: remove and use BasicViewFactory instead
public class LoginViewFactory {
    private final Game game;

    public LoginViewFactory(final Game game) {
        this.game = game;
    }
    public AbstractView getLoginView(final RequestHandler requestHandler, LoginStateSender authenticatingSocket, AbstractViewManager viewManager) {
        return new LoginView(
                game,
                new LoginViewEventListener(requestHandler, authenticatingSocket),
                viewManager
        );
    }
}
