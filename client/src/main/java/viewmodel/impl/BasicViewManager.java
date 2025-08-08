package viewmodel.impl;

import network.client.ClientSideSocketWrapper;
import viewmodel.TextureManager;
import viewmodel.ViewManager;

public class BasicViewManager implements ViewManager {
    private final BasicViewFactory viewFactory;
    private final BasicTextureManager textureManager;

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
    public TextureManager getTextureManager() {
        return textureManager;
    }

    @Override
    public void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper) {
        viewFactory.getGameClientView(clientSideSocketWrapper).display();
    }

    @Override
    public void moveToMainMenu() {
        viewFactory.getMainMenuView().display();
    }

    @Override
    public void moveToPlayView() {
        viewFactory.getPlayView().display();
    }

    @Override
    public void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
        viewFactory.getLoginView(clientSideSocketWrapper).display();
    }

}
