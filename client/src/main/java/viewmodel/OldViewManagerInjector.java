package viewmodel;

import com.badlogic.gdx.Game;

import frontend.concreteviews.loginview.LoginViewFactory;
import network.concretesocketwrapperfactory.ConcreteSocketWrapperFactory;
import frontend.concreteviewproviders.AdminViewProvider;
import frontend.concreteviewproviders.UserViewProvider;

            //TODO remove: transfer all tasks to proper classes and remove this
public class OldViewManagerInjector {
    private final Game game;

    public OldViewManagerInjector(final Game game) {
        this.game = game;
    }

    public OldViewManager getViewManager() {
        final var loginViewFactory = new LoginViewFactory(game);
        final var abstractSocketWrapperFactory = new ConcreteSocketWrapperFactory();
        final var userViewProvider = new UserViewProvider(game);
        final var adminViewProvider = new AdminViewProvider(game);

        return new OldViewManager(userViewProvider, abstractSocketWrapperFactory, adminViewProvider, loginViewFactory);
    }
}
