package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;

import network.client.ClientSideSocketWrapper;
import viewmodel.AbstractViewManager;
import viewmodel.AbstractView;

//TODO view: remove and use BasicViewFactory instead
public class LoginViewFactory {
    private final Game game;

    public LoginViewFactory(final Game game) {
        this.game = game;
    }

    public AbstractView getLoginView(AbstractViewManager viewManager, ClientSideSocketWrapper clientSideSocketWrapper) {
        return new LoginView(
                game,
                new LoginViewEventListener(viewManager, clientSideSocketWrapper),
                viewManager.getTextureManager(),
                viewManager);
    }
}
