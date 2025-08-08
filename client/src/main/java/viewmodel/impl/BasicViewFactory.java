package viewmodel.impl;

import com.badlogic.gdx.Game;
import frontend.concreteviews.basicviews.MainMenuView;
import frontend.concreteviews.basicviews.NotImplementedView;
import frontend.concreteviews.basicviews.PlayView;
import frontend.concreteviews.gameclientview.GameClientViewInjector;
import frontend.concreteviews.loginview.LoginViewFactory;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapperFactory;
import viewmodel.View;
import viewmodel.ViewFactory;
import viewmodel.ViewManager;

public class BasicViewFactory implements ViewFactory {
    private final Game game;
    private final ClientSideSocketWrapperFactory clientSideSocketWrapperFactory;
    ViewManager viewManager;

    BasicViewFactory(Game game, ClientSideSocketWrapperFactory clientSideSocketWrapperFactory) {
        this.game = game;
        this.clientSideSocketWrapperFactory = clientSideSocketWrapperFactory;
    }

    // TODO hardcoded: remove hardcoded strings, use config instead
    void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public View getMainMenuView() {
        return new MainMenuView(game, viewManager);
    }

    public View getSettingsView() {
        return new NotImplementedView(game, viewManager);
    }

    public View getPlayView() {
        return new PlayView(game, viewManager, clientSideSocketWrapperFactory);
    }

    @Override
    public View getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new GameClientViewInjector(game, viewManager.getTextureManager(), viewManager)
                .getGameClientView(clientSideSocketWrapper);
    }

    @Override
    public View getLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new LoginViewFactory(game).getLoginView(viewManager, clientSideSocketWrapper);
    }
}
