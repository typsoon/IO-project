package viewmodel.impl;

import com.badlogic.gdx.Game;

import network.client.ClientSideSocketWrapperFactory;
import viewmodel.ViewManager;

public class BasicViewManagerInjector {
    private final Game game;

    public BasicViewManagerInjector(Game game) {
        this.game = game;
    }

    public ViewManager getViewManager() {
        final var textureManager = new BasicTextureManager();
        final var clientSideSocketWrapperFactory = new ClientSideSocketWrapperFactory();
        final BasicViewFactory viewFactory = new BasicViewFactory(game, clientSideSocketWrapperFactory);

        // TODO view: add more functionality
        return new BasicViewManager(
                viewFactory,
                textureManager);
    }
}
