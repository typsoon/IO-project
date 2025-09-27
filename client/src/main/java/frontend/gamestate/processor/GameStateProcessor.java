package frontend.gamestate.processor;

import frontend.gamestate.DrawableInfo;
import frontend.gamestate.EntityVisibleState;
import frontend.gamestate.IDisplayableGameState;
import game.engine.PlayerConfig;
import game.engine.modules.IGeometryModule;
import game.gamestates.EntityState;
import game.gamestates.IGameState;
import game.gamestates.PlayerInventoryState;
import game.gamestates.PlayerState;
import viewmodel.game.RenderableObjectFactory;
import viewmodel.game.RenderablePlayer;
import viewmodel.game.TimedRenderableObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameStateProcessor implements IGameStateProcessor {

    private static final float TIME_STEP = 1f / 64f;
    private float accumulatedTime = 0f;
    private final float timeThreshold = 0.5f;

    private final RenderablePlayer player;

    IGeometryModule geometryModule;
    RenderableObjectFactory renderableObjectFactory;
    IDisplayableGameState displayableGameState;
    Map<Integer, TimedRenderableObject> entities = new HashMap<>();
    Map<Integer, DrawableInfo> entityItems = new HashMap<>();


    public GameStateProcessor(IGeometryModule geometryModule, RenderableObjectFactory objectFactory,
                              IDisplayableGameState displayableGameState, PlayerConfig playerConfig,
                              Collection<IGameState> initialGameStates) {
        this.geometryModule = geometryModule;
        this.renderableObjectFactory = objectFactory;
        this.displayableGameState = displayableGameState;
        this.player = objectFactory.createRenderablePlayer(playerConfig);
        this.displayableGameState.addPlayer(player);
        this.displayableGameState.addDrawable(player.getDrawableInfo());

        processGameStates(initialGameStates, 0);
    }

    @Override
    public void processGameStates(Collection<IGameState> gameStates, float deltaTime) {
        accumulatedTime += deltaTime;
        while (accumulatedTime >= TIME_STEP) {
            geometryModule.cycle();
            accumulatedTime -= TIME_STEP;
        }
        for (IGameState gameState : gameStates) {
            switch (gameState) {
                case EntityState entityState -> updateEntityState(entityState);
                case PlayerState playerState -> updatePlayerState(playerState);
                case PlayerInventoryState playerInventoryState -> updatePlayerInventoryState(playerInventoryState);
                default -> {
                }
            }
        }
        cleanupEntities(deltaTime);
    }

    private void updatePlayerState(PlayerState playerState) {
        player.getDrawable().setState(EntityVisibleState.values()[playerState.action().ordinal()]);
        player.getDrawable().setStateTime(playerState.actionProgress());

        player.setPosition(playerState.position());
        player.setVelocity(playerState.velocity());
        player.setRotation(playerState.rotation());

        player.setRange(playerState.sightRange());
        player.setMaxHp(playerState.maxHp());
        player.setCurrentHp(playerState.currentHp());
    }

    private void updatePlayerInventoryState(PlayerInventoryState playerInventoryState) {
        player.setInventoryInfo(playerInventoryState.inventory());
    }


    private void updateEntityState(EntityState entityState) {
        TimedRenderableObject renderableObject;
        if (entities.containsKey(entityState.entityId())) {
            renderableObject = entities.get(entityState.entityId());
        } else {
            renderableObject = renderableObjectFactory.createRenderableObject(entityState);
            entities.put(entityState.entityId(), renderableObject);
            displayableGameState.addDrawable(renderableObject.getDrawable());
        }

        renderableObject.getDrawable().setState(EntityVisibleState.values()[entityState.action().ordinal()]);
        renderableObject.getDrawable().setStateTime(entityState.actionProgress());
        renderableObject.setPosition(entityState.position());
        renderableObject.setVelocity(entityState.velocity());
        renderableObject.setRotation(entityState.rotation());

        renderableObject.timeSinceUpdate = 0f;
    }

    private void cleanupEntities(float deltaTime) {
        entities.entrySet().removeIf(entry -> {
            TimedRenderableObject renderableObject = entry.getValue();
            renderableObject.timeSinceUpdate += deltaTime;
            if (renderableObject.timeSinceUpdate >= timeThreshold) {
                displayableGameState.removeDrawable(renderableObject.getDrawable());
                renderableObject.dispose();
                return true;
            }
            return false;
        });
    }
}
