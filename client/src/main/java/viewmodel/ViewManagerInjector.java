package viewmodel;

import com.badlogic.gdx.Game;

import frontend.concreteviews.loginview.LoginViewFactory;
import network.concretesocketwrapperfactory.ConcreteSocketWrapperFactory;
import frontend.concreteviewproviders.AdminViewProvider;
import frontend.concreteviewproviders.UserViewProvider;

public class ViewManagerInjector {
    private final Game game;

    public ViewManagerInjector(final Game game) {
        this.game = game;
    }

    public ViewManager getViewManager() {
        final var loginViewFactory = new LoginViewFactory(game);
        final var abstractSocketWrapperFactory = new ConcreteSocketWrapperFactory();
        final var userViewProvider = new UserViewProvider(game);
        final var adminViewProvider = new AdminViewProvider(game);

        return new ViewManager(userViewProvider, abstractSocketWrapperFactory, adminViewProvider, loginViewFactory);
    }
}
