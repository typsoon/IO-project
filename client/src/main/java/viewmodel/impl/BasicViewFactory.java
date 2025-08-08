package viewmodel.impl;

import com.badlogic.gdx.Game;
import frontend.concreteviews.basicviews.MainMenuView;
import frontend.concreteviews.basicviews.NotImplementedView;
import frontend.concreteviews.basicviews.PlayView;
import frontend.concreteviews.gameclientview.GameClientViewFactory;
import frontend.concreteviews.loginview.LoginViewFactory;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapperFactory;
import viewmodel.IView;
import viewmodel.IViewFactory;
import viewmodel.IViewManager;

public class BasicViewFactory implements IViewFactory {
    private final Game game;
    private final ClientSideSocketWrapperFactory clientSideSocketWrapperFactory;
    IViewManager viewManager;

    BasicViewFactory(Game game, ClientSideSocketWrapperFactory clientSideSocketWrapperFactory) {
        this.game = game;
        this.clientSideSocketWrapperFactory = clientSideSocketWrapperFactory;
    }

    // TODO hardcoded: remove hardcoded strings, use config instead
    void setViewManager(IViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public IView getMainMenuView() {
        return new MainMenuView(game, viewManager);
    }

    public IView getSettingsView() {
        return new NotImplementedView(game, viewManager);
    }

    public IView getPlayView() {
        return new PlayView(game, viewManager, clientSideSocketWrapperFactory);
    }

    @Override
    public IView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new GameClientViewFactory(game, viewManager.getTextureManager(), viewManager)
                .getGameClientView(clientSideSocketWrapper);
    }

    @Override
    public IView getLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new LoginViewFactory(game).getLoginView(viewManager, clientSideSocketWrapper);
    }
}
