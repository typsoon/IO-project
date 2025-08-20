package frontend.concreteviews.gameplayview;

import java.util.Collection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameclientview.GameClientView;
import frontend.concreteviews.loginview.LoginView;
import frontend.gamestate.IReadOnlyDisplayableGameState;
import game.utility.Point2F;
import game.utility.Vector2F;
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
    @SuppressWarnings("unused")
    private final Game game;
    private final Collection<EventListener> gameplayViewEventListeners;
    private final Collection<InputProcessor> gameplayViewInputProcessors;
    private final ITextureManager textureManager;
    private final IReadOnlyDisplayableGameState gameState;
    public final static int WINDOW_WIDTH = 900;
    public final static int WINDOW_HEIGHT = 900;
    public final ShapeRenderer shapeDrawer = new ShapeRenderer();
    private final TexturesProvider texturesProvider;

    private EntitiesDrawer entitiesDrawer;

    private Stage stage;
    private OrthographicCamera gameCamera;
    private FitViewport viewport;

    // private final TextureRegion test;
    // private final SpriteBatch spriteBatch = new SpriteBatch();

    private final Point2F getCameraPosition() {
        var playerDrawableInfo = gameState.getPlayerData().iterator().next().getDrawableInfo();
        // Logger.getGlobal().info("%f %f".formatted(playerDrawableInfo.getX(),
        // playerDrawableInfo.getY()));
        return new Point2F(playerDrawableInfo.getX(), playerDrawableInfo.getY());
    }

    private final Vector2F getVisibilityRange() {
        var playerRangeOfView = gameState.getPlayerData().iterator().next().getRange();
        return playerRangeOfView;
    }

    GameplayView(Game game, Collection<EventListener> gameplayViewEventListeners,
            Collection<InputProcessor> gameplayViewInputProcessors,
            ITextureManager textureManager, IReadOnlyDisplayableGameState gameState,
            TexturesProvider texturesProvider) {
        this.game = game;
        this.gameplayViewEventListeners = gameplayViewEventListeners;
        this.gameplayViewInputProcessors = gameplayViewInputProcessors;
        this.textureManager = textureManager;
        this.gameState = gameState;
        this.texturesProvider = texturesProvider;

        // test = texturesProvider.getTextureRegion(EntityGroupID.HUMAN_BASIC,
        // EntityVisibleState.IDLE_FRONT, 0);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        stage.act(delta);
        stage.draw();

        var cameraPos = getCameraPosition();
        gameCamera.position.set(cameraPos.x(), cameraPos.y(), 0);

        var rangeOfView = getVisibilityRange();
        viewport.setWorldSize(rangeOfView.x(), rangeOfView.y());
        viewport.apply();

        var drawableInfos = gameState.getSpritesReadonly();
        entitiesDrawer.drawEntities(drawableInfos);
    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

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

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false);

        viewport = new FitViewport(0, 0, gameCamera);
        // viewport = new FitViewport(WIDTH, HEIGHT);

        entitiesDrawer = new EntitiesDrawer(texturesProvider, viewport);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
