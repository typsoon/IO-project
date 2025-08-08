package frontend.concreteviews.gameplayview;

import java.util.Collection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import frontend.concreteviews.gameclientview.GameClientView;
import frontend.concreteviews.loginview.LoginView;

import frontend.gamestate.IReadOnlyDisplayableGameState;
import viewmodel.ITextureManager;

//NOTE: this class doesn't implement View, nor does it contain GameplayManager. We use ViewWithEventLoop class to wrap
//GameplayView and provide required functionalities

/**
 * This class serves as a {@link Screen} displayed during the gameplay, it uses
 * {@link EventListener}s and {@link InputProcessor}s to
 * handle user input
 * 
 * @see LoginView
 * @see GameClientView
 */
public class GameplayView extends ScreenAdapter {
    private final Game game;
    private final Collection<EventListener> gameplayViewEventListeners;
    private final Collection<InputProcessor> gameplayViewInputProcessors;
    private final ITextureManager textureManager;
    private final IReadOnlyDisplayableGameState gameState;
    private Stage stage;

    GameplayView(Game game, Collection<EventListener> gameplayViewEventListeners,
                 Collection<InputProcessor> gameplayViewInputProcessors,
                 ITextureManager textureManager, IReadOnlyDisplayableGameState gameState) {
        this.game = game;
        this.gameplayViewEventListeners = gameplayViewEventListeners;
        this.gameplayViewInputProcessors = gameplayViewInputProcessors;
        this.textureManager = textureManager;
        this.gameState = gameState;
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        stage = new Stage();
        for (EventListener eventListener : gameplayViewEventListeners) {
            stage.addListener(eventListener);
        }

        var multiplexer = new InputMultiplexer(stage);
        for (var processor : gameplayViewInputProcessors) {
            multiplexer.addProcessor(processor);
        }
        Gdx.input.setInputProcessor(multiplexer);

        final Table table = textureManager.getTable();

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
