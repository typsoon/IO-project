package frontend.concreteviews.gameplayview;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameclientview.GameClientView;
import frontend.concreteviews.gameplayview.impl.GameplayInfoProviderImpl;
import frontend.concreteviews.gameplayview.processors.ProcessorFactoriesCreator.DataNeededForCreation;
import frontend.concreteviews.loginview.LoginView;
import frontend.gamestate.IReadOnlyDisplayableGameState;
import game.utility.Point2F;
import game.utility.Vector2F;
import utils.ObserverWithATwist.Subscribable;
import viewmodel.ITextureManager;

import java.util.Collection;
import java.util.function.Function;

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
    private final Collection<Function<DataNeededForCreation, InputProcessor>> gameplayViewInputProcessorsFactories;

    private final ITextureManager textureManager;
    private final IReadOnlyDisplayableGameState gameState;
    public final static int WINDOW_WIDTH = 900;
    public final static int WINDOW_HEIGHT = 900;
    public final ShapeRenderer shapeDrawer = new ShapeRenderer();
    private final TexturesProvider texturesProvider;
    private final Subscribable gameCycles;

    private EntitiesDrawer entitiesDrawer;

    private Stage stage;
    private OrthographicCamera gameCamera;
    private FitViewport viewport;
    private IGameplayInfoProvider gameplayInfoProvider;

    // private final TextureRegion test;
    // private final SpriteBatch spriteBatch = new SpriteBatch();

    private final Point2F getCameraPosition() {
        var playerDrawableInfo = gameState.getPlayerData().iterator().next().getDrawableInfo();
        return new Point2F(playerDrawableInfo.getX(), playerDrawableInfo.getY());
    }

    private final Vector2F getVisibilityRange() {
        var playerRangeOfView = gameState.getPlayerData().iterator().next().getRange();
        return playerRangeOfView;
    }

    GameplayView(Game game, Collection<EventListener> gameplayViewEventListeners,
                 Collection<Function<DataNeededForCreation, InputProcessor>> gameplayViewInputProcessorsFactories,
                 ITextureManager textureManager, IReadOnlyDisplayableGameState gameState,
                 TexturesProvider texturesProvider, Subscribable gameCycles) {
        this.game = game;
        this.gameplayViewEventListeners = gameplayViewEventListeners;
        this.gameplayViewInputProcessorsFactories = gameplayViewInputProcessorsFactories;
        this.textureManager = textureManager;
        this.gameState = gameState;
        this.texturesProvider = texturesProvider;
        this.gameCycles = gameCycles;

        // test = texturesProvider.getTextureRegion(EntityGroupID.HUMAN_BASIC,
        // EntityVisibleState.IDLE_FRONT, 0);
    }

    // this in only for debug
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private void grid() {
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        float camX = gameCamera.position.x;
        float camY = gameCamera.position.y;

        Vector2F rangeOfView = getVisibilityRange();

        int gridSize = 2;
        float worldWidth = rangeOfView.x();
        float worldHeight = rangeOfView.y();
        int startX = (int) (camX - worldWidth / 2) / gridSize * gridSize;
        int endX = (int) (camX + worldWidth / 2);

        int startY = (int) (camY - worldHeight / 2) / gridSize * gridSize;
        int endY = (int) (camY + worldHeight / 2);

        for (int x = startX; x <= endX; x += gridSize) {
            shapeRenderer.line(x, camY - worldHeight / 2, x, camY + worldHeight / 2);
        }
        for (int y = startY; y <= endY; y += gridSize) {
            shapeRenderer.line(camX - worldWidth / 2, y, camX + worldWidth / 2, y);
        }

        shapeRenderer.end();
    }

    private static final float eps = 1e-9f;

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 0);

        stage.act(delta);
        stage.draw();

        var cameraPos = getCameraPosition();
        gameCamera.position.set(cameraPos.x(), cameraPos.y(), 0);

        var rangeOfView = getVisibilityRange();
        viewport.setWorldSize(rangeOfView.x() + eps, rangeOfView.y() + eps);
        viewport.apply();

        // this is for debug puposes, to see the grid
        grid();

        var drawableInfos = gameState.getSpritesReadonly();
        entitiesDrawer.drawEntities(drawableInfos);
        //TODO add gui
    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void show() {
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false);
        viewport = new FitViewport(0, 0, gameCamera);
        gameplayInfoProvider = new GameplayInfoProviderImpl(viewport, this::getCameraPosition);
        entitiesDrawer = new EntitiesDrawer(texturesProvider, viewport);
        stage = new Stage();

        final var rangeOfView = getVisibilityRange();
        viewport.setWorldSize(rangeOfView.x() + eps, rangeOfView.y() + eps);
        viewport.apply();

        Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

        for (EventListener eventListener : gameplayViewEventListeners) {
            stage.addListener(eventListener);
        }
        final Table table = textureManager.getTable();
        stage.addActor(table);

        var multiplexer = new InputMultiplexer(stage);
        for (var processorFactory : gameplayViewInputProcessorsFactories) {
            multiplexer
                    .addProcessor(processorFactory.apply(new DataNeededForCreation(gameplayInfoProvider, gameCycles)));
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
