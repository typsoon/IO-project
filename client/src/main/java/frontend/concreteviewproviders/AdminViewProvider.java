package frontend.concreteviewproviders;

import com.badlogic.gdx.Game;

import viewmodel.AbstractView;
import viewmodel.AbstractViewProvider;

public class AdminViewProvider implements AbstractViewProvider {
    private final Game game;

    public AdminViewProvider(final Game game) {
        this.game = game;
    }

}
