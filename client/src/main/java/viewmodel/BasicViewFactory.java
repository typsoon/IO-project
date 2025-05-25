package viewmodel;

import com.badlogic.gdx.Game;
import frontend.concreteviews.basicviews.MainMenuView;
import frontend.concreteviews.basicviews.NotImplementedView;
import frontend.concreteviews.basicviews.PlayView;

public class BasicViewFactory implements AbstractViewFactory {
    Game game;
    AbstractViewManager viewManager;
    BasicViewFactory(Game game) {
        this.game = game;
    }
    //TODO hardcoded: remove hardcoded strings, use config instead
    void setViewManager(AbstractViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public AbstractView getMainMenuView() {
        return new MainMenuView(game, viewManager);
    }

    public AbstractView getSettingsView() {
        return new NotImplementedView(game, viewManager);
    }

    public AbstractView getPlayView() {
        return new PlayView(game, viewManager);
    }

}
