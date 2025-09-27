package game.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import game.actions.*;
import game.engine.GameEngine;
import game.engine.PlayerConfig;
import game.engine.entities.EntityGroupID;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.modules.GeometryModule;
import game.gamestates.IGameState;
import game.session.*;
import game.utility.Vector2F;

import java.util.List;

public class DebugScreen implements Screen {
    private final OrthographicCamera camera;
    private final Box2DDebugRenderer debugRenderer;
    private final GameSessionManager sessionManager;
    private final LocalPlayerConnector player1Connector;
    private final LocalPlayerConnector player2Connector;
    private final GeometryModule geometryModule;

    public DebugScreen() {
        camera = new OrthographicCamera(20, 20);
        camera.position.set(0, 0, 0);
        camera.update();
        debugRenderer = new Box2DDebugRenderer();

        // Create connectors
        player1Connector = new LocalPlayerConnector();
        player2Connector = new LocalPlayerConnector();

        // Create session manager
        PlayerConfig config = new PlayerConfig(GeometryConfigID.HUMAN, EntityGroupID.HUMAN_BASIC);
        List<PlayerData> players = List.of(
                new PlayerData(player1Connector, config),
                new PlayerData(player2Connector, config));
        sessionManager = GameSessionFactory.createGameSessionManager(players);
        sessionManager.startGameLoop();

        // Get geometry module for debug rendering
        geometryModule = (GeometryModule) ((GameEngine) sessionManager.getGameEngine()).getGeometryModule();
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(geometryModule.getWorld(), camera.combined);
    }

    private void handleInput() {
        Direction dir1 = getDirection(
                Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D);
        if (dir1 != null) {
            player1Connector.sendAction(new PlayerMove(dir1));
        }
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float worldX = worldCoords.x - camera.position.x;
        float worldY = worldCoords.y - camera.position.y;


        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            player2Connector.sendAction(new PlayerSlotUse(UsageType.PRIMARY,
                    new Vector2F(worldCoords.x, worldCoords.y).normalize(), 0));
        }

        Direction dir2 = getDirection(
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT);
        if (dir2 != null) {
            player2Connector.sendAction(new PlayerMove(dir2));
        }
    }

    private Direction getDirection(int up, int down, int left, int right) {
        boolean u = Gdx.input.isKeyPressed(up);
        boolean d = Gdx.input.isKeyPressed(down);
        boolean l = Gdx.input.isKeyPressed(left);
        boolean r = Gdx.input.isKeyPressed(right);
        if (u && r)
            return Direction.NE;
        if (u && l)
            return Direction.NW;
        if (d && r)
            return Direction.SE;
        if (d && l)
            return Direction.SW;
        if (u)
            return Direction.N;
        if (d)
            return Direction.S;
        if (l)
            return Direction.W;
        if (r)
            return Direction.E;
        return Direction.NONE;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        sessionManager.dispose();
    }

    // Local connector for debug input
    private static class LocalPlayerConnector implements ISubscribablePlayerConnector {
        private IActionReceiver receiver;

        @Override
        public void subscribe(IActionReceiver receiver) {
            this.receiver = receiver;
        }

        @Override
        public void unsubscribe(IActionReceiver receiver) {
            this.receiver = null;
        }

        @Override
        public void sendGameStates(java.util.Collection<IGameState> gameStates) {
            // for (game.gamestates.GameState gameState : gameStates) {
            // // For debug, we can just print the game state or handle it as needed
            // System.out.println("Game State: " + gameState);
            // }
        }

        public void sendAction(IAction action) {
            if (receiver != null)
                receiver.sendAction(this, action);
        }
    }
}
