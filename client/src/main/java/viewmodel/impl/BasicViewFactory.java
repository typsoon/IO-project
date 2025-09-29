package viewmodel.impl;

import java.util.Collection;

import com.badlogic.gdx.Game;

import frontend.assetsloading.impl.AtlasLoader;
import frontend.concreteviews.basicviews.MainMenuView;
import frontend.concreteviews.basicviews.NotImplementedView;
import frontend.concreteviews.basicviews.PlayView;
import frontend.concreteviews.gameclientview.GameClientViewFactory;
import frontend.concreteviews.gameplayview.GameplayViewFactory;
import frontend.concreteviews.loginview.LoginViewFactory;
import game.engine.PlayerConfig;
import game.gamestates.IGameState;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapperFactory;
import viewmodel.IView;
import viewmodel.IViewFactory;
import viewmodel.IViewManager;

public class BasicViewFactory implements IViewFactory {
    private final Game game;
    private final ClientSideSocketWrapperFactory clientSideSocketWrapperFactory;
    private IViewManager viewManager;

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
    public IView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper, int id) {
        return new GameClientViewFactory(game, viewManager.getTextureManager(), viewManager)
                .getGameClientView(clientSideSocketWrapper, id);
    }

    @Override
    public IView getLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new LoginViewFactory(game).getLoginView(viewManager, clientSideSocketWrapper);
    }

    @Override
    public IView getGameplayView(ClientSideSocketWrapper clientSideSocketWrapper, int id,
            Collection<IGameState> initialGameStates) {
        return new GameplayViewFactory().getGameplayView(game,
                viewManager,
                clientSideSocketWrapper,
                viewManager.getTextureManager(),
                new PlayerConfig(), new AtlasLoader(), initialGameStates, id);
    }
}
