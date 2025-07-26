package viewmodel.impl;

import com.badlogic.gdx.Game;
import frontend.concreteviews.basicviews.MainMenuView;
import frontend.concreteviews.basicviews.NotImplementedView;
import frontend.concreteviews.basicviews.PlayView;
import frontend.concreteviews.gameclientview.GameClientView;
import frontend.concreteviews.loginview.LoginViewFactory;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapperFactory;
import viewmodel.AbstractView;
import viewmodel.AbstractViewFactory;
import viewmodel.AbstractViewManager;

public class BasicViewFactory implements AbstractViewFactory {
    private final Game game;
    private final ClientSideSocketWrapperFactory clientSideSocketWrapperFactory;
    AbstractViewManager viewManager;

    BasicViewFactory(Game game, ClientSideSocketWrapperFactory clientSideSocketWrapperFactory) {
        this.game = game;
        this.clientSideSocketWrapperFactory = clientSideSocketWrapperFactory;
    }

    // TODO hardcoded: remove hardcoded strings, use config instead
    void setViewManager(AbstractViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public AbstractView getMainMenuView() {
        return new MainMenuView(game, viewManager);
    }

    public AbstractView getSettingsView() {
        return new NotImplementedView(game, viewManager);
    }

    public AbstractView getPlayView() {
        return new PlayView(game, viewManager, clientSideSocketWrapperFactory);
    }

    @Override
    public AbstractView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new GameClientView(game, viewManager);
    }

    @Override
    public AbstractView getLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new LoginViewFactory(game).getLoginView(viewManager, clientSideSocketWrapper);
    }
}
