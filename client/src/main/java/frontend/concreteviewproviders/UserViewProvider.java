package frontend.concreteviewproviders;

import com.badlogic.gdx.Game;

import viewmodel.AbstractViewProvider;

public class UserViewProvider implements AbstractViewProvider {
    private final Game game;

    public UserViewProvider(final Game game) {
        this.game = game;
    }
}
