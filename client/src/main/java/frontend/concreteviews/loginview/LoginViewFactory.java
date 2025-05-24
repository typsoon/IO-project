package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;

import network.socketwrappers.SenderTypes.LoginStateSender;
import viewmodel.AbstractLoginViewFactory;
import viewmodel.AbstractLoginView;
import viewmodel.RequestHandler;

public class LoginViewFactory implements AbstractLoginViewFactory {
    private final Game game;

    public LoginViewFactory(final Game game) {
        this.game = game;
    }

    public AbstractLoginView getLoginView(final RequestHandler requestHandler, LoginStateSender authenticatingSocket) {
        final var loginViewInputAdapter = new LoginViewEventListener(requestHandler, authenticatingSocket);
        return new LoginView(game, loginViewInputAdapter);
    }
}
