package viewmodel.impl;

import com.badlogic.gdx.Game;

import frontend.assetsloading.impl.BasicTextureManager;
import network.client.ClientSideSocketWrapperFactory;
import frontend.assetsloading.*;
import frontend.assetsloading.impl.*;
import viewmodel.*;

public class BasicViewManagerInjector {
    private final Game game;

    public BasicViewManagerInjector(Game game) {
        this.game = game;
    }

    public IViewManager getViewManager() {
        final var textureManager = new BasicTextureManager();
        final var clientSideSocketWrapperFactory = new ClientSideSocketWrapperFactory();
        final BasicViewFactory viewFactory = new BasicViewFactory(game, clientSideSocketWrapperFactory);

        // TODO view: add more functionality
        return new BasicViewManager(
                viewFactory,
                textureManager);
    }
}
