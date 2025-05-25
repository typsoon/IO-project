package viewmodel;

import com.badlogic.gdx.Game;

public class BasicViewManagerInjector {
    private final Game game;

    public BasicViewManagerInjector(Game game) {
        this.game = game;
    }

    public AbstractViewManager getViewManager() {
        final var textureManager = new BasicTextureManager();
        final BasicViewFactory viewFactory = new BasicViewFactory(game);

        //TODO view: add more functionality
        return new BasicViewManager(
                viewFactory,
                textureManager
        );
    }
}
