package frontend.concreteviews.mainmenu;

import com.badlogic.gdx.Game;

import viewmodel.AbstractDefaultViewManager;
import viewmodel.AbstractMainMenuViewFactory;
import viewmodel.AbstractView;

public class MainMenuViewFactory implements AbstractMainMenuViewFactory {
    private final Game game;
    private AbstractDefaultViewManager viewManager;

    public MainMenuViewFactory(final Game game) {
        this.game = game;
    }

    public void setViewManager(AbstractDefaultViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public AbstractView getMainMenuView() {
        return new MainMenuView(game, viewManager);
    }
}
