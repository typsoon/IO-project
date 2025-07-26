package viewmodel.impl;

import network.client.ClientSideSocketWrapper;
import viewmodel.AbstractTextureManager;
import viewmodel.AbstractViewFactory;
import viewmodel.AbstractViewManager;

public class BasicViewManager implements AbstractViewManager {
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
        viewFactory.getMainMenuView().display();
        ;
    }

    @Override
    public AbstractViewFactory getViewFactory() {
        return viewFactory;
    }

    @Override
    public AbstractTextureManager getTextureManager() {
        return textureManager;
    }

    @Override
    public void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveToGameClient'");
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
