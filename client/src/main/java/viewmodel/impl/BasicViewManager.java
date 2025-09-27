package viewmodel.impl;

import java.util.Collection;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.impl.BasicTextureManager;
import game.gamestates.IGameState;
import network.client.ClientSideSocketWrapper;
import viewmodel.IViewFactory;
import viewmodel.IViewManager;
import frontend.assetsloading.*;

public class BasicViewManager implements IViewManager {
    private final IViewFactory viewFactory;
    private final ITextureManager textureManager;

    // should only be called by its injector
    BasicViewManager(
            BasicViewFactory basicViewFactory,
            BasicTextureManager basicTextureManager) {
        this.viewFactory = basicViewFactory;
        basicViewFactory.setViewManager(this);
        this.textureManager = basicTextureManager;
    }

    @Override
    public void start() {
        var activeView = viewFactory.getMainMenuView();
        activeView.display();
    }

    @Override
    public ITextureManager getTextureManager() {
        return textureManager;
    }

    @Override
    public void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper, int id) {
        viewFactory.getGameClientView(clientSideSocketWrapper, id).display();
    }

    @Override
    public void moveToMainMenu() {
        viewFactory.getMainMenuView().display();
    }

    @Override
    public void moveToSettings() {
        viewFactory.getSettingsView().display();
    }

    @Override
    public void moveToPlayView() {
        viewFactory.getPlayView().display();
    }

    @Override
    public void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
        viewFactory.getLoginView(clientSideSocketWrapper).display();
    }

    @Override
    public void moveToGameplay(ClientSideSocketWrapper clientSideSocketWrapper, int id,
            Collection<IGameState> initialGameStates) {
        viewFactory.getGameplayView(clientSideSocketWrapper, id, initialGameStates).display();
    }

}
