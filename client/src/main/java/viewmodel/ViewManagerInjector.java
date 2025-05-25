package viewmodel;

import com.badlogic.gdx.Game;
import frontend.concreteviews.mainmenu.MainMenuViewFactory;

public class ViewManagerInjector {
    private final Game game;

    public ViewManagerInjector(Game game) {
        this.game = game;
    }

    public AbstractDefaultViewManager getViewManager() {
        final var mainMenuFactory = new MainMenuViewFactory(game);
        //TODO: add more (all) screen adapters
        return new ViewManager(mainMenuFactory);
    }
}
