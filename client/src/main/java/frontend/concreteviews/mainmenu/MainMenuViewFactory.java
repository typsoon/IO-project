package frontend.concreteviews.mainmenu;

import com.badlogic.gdx.Game;

import viewmodel.AbstractMainMenuViewFactory;
import viewmodel.AbstractView;
import viewmodel.RequestHandler;

public class MainMenuViewFactory implements AbstractMainMenuViewFactory {
    private final Game game;

    public MainMenuViewFactory(final Game game) {
        this.game = game;
    }

    public AbstractView getMainMenuView() {
        return new MainMenuView(game);
    }
}
