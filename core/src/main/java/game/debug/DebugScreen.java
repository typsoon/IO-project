package game.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import game.session.ActionReceiver;
import game.session.PlayerConnector;
import game.session.PlayerData;
import game.actions.Direction;
import game.actions.PlayerMove;
import game.engine.GameEngineImplementation;
import game.engine.PlayerConfig;
import game.engine.entities.GeometryConfigID;
import game.engine.modules.GeometryModuleImplementation;
import game.session.GameSessionFactory;
import game.session.GameSessionManager;

import java.io.IOException;
import java.util.List;

public class DebugScreen implements Screen {
    private final OrthographicCamera camera;
    private final Box2DDebugRenderer debugRenderer;
    private final GameSessionManager sessionManager;
    private final LocalPlayerConnector player1Connector;
    private final LocalPlayerConnector player2Connector;
    private final GeometryModuleImplementation geometryModule;

    public DebugScreen() {
        camera = new OrthographicCamera(20, 20);
        camera.position.set(0, 0, 0);
        camera.update();
        debugRenderer = new Box2DDebugRenderer();

        // Create connectors
        player1Connector = new LocalPlayerConnector();
        player2Connector = new LocalPlayerConnector();

        // Create session manager
        PlayerConfig config = new PlayerConfig(GeometryConfigID.HUMAN);
        List<PlayerData> players = List.of(
                new PlayerData(player1Connector, config),
                new PlayerData(player2Connector, config)
        );
        sessionManager = GameSessionFactory.createGameSessionManager(players);
        sessionManager.startGameLoop();

        // Get geometry module for debug rendering
        geometryModule = (GeometryModuleImplementation) ((GameEngineImplementation) sessionManager.getGameEngine()).getGeometryModule();
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
                Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D
        );
        if (dir1 != null) {
            player1Connector.sendAction( new PlayerMove(dir1));
        }
        Direction dir2 = getDirection(
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT
        );
        if (dir2 != null) {
            player2Connector.sendAction(new PlayerMove(dir2));
        }
    }

    private Direction getDirection(int up, int down, int left, int right) {
        boolean u = Gdx.input.isKeyPressed(up);
        boolean d = Gdx.input.isKeyPressed(down);
        boolean l = Gdx.input.isKeyPressed(left);
        boolean r = Gdx.input.isKeyPressed(right);
        if (u && r) return Direction.NE;
        if (u && l) return Direction.NW;
        if (d && r) return Direction.SE;
        if (d && l) return Direction.SW;
        if (u) return Direction.N;
        if (d) return Direction.S;
        if (l) return Direction.W;
        if (r) return Direction.E;
        return null;
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override
    public void dispose() {
        debugRenderer.dispose();
        try {
            sessionManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Local connector for debug input
    private static class LocalPlayerConnector implements PlayerConnector {
        private ActionReceiver receiver;
        @Override
        public void subscribe(ActionReceiver receiver) { this.receiver = receiver; }
        @Override
        public void unsubscribe(ActionReceiver receiver) { this.receiver = null; }
        @Override
        public void sendGameState(java.util.Collection<game.gamestates.GameState> gameStates) {
//            for (game.gamestates.GameState gameState : gameStates) {
//                // For debug, we can just print the game state or handle it as needed
//                System.out.println("Game State: " + gameState);
//            }
        }

        public void sendAction(game.actions.Action action) {
            if (receiver != null) receiver.sendAction(this, action);
        }
    }
}