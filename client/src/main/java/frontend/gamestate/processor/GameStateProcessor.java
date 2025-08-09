package frontend.gamestate.processor;

import game.engine.modules.IGeometryModule;
import game.gamestates.EntityState;
import game.gamestates.IGameState;
import viewmodel.game.TimedRenderableObjectFactory;
import viewmodel.game.TimedRenderableObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameStateProcessor implements IGameStateProcessor {

    private static final float TIME_STEP = 1f/64f;
    private float accumulatedTime = 0f;
    private final float timeThreshold = 0.5f;

    IGeometryModule geometryModule;
    TimedRenderableObjectFactory renderableObjectFactory;

    Map<Integer, TimedRenderableObject> entities = new HashMap<>();

    GameStateProcessor(IGeometryModule geometryModule, TimedRenderableObjectFactory objectFactory) {
        this.geometryModule = geometryModule;
        this.renderableObjectFactory = objectFactory;
    }

    @Override
    public void processGameStates(Collection<IGameState> gameStates, float deltaTime) {
        accumulatedTime += deltaTime;
        while (accumulatedTime >= TIME_STEP) {
            geometryModule.cycle();
            accumulatedTime -= TIME_STEP;
        }
        for (IGameState gameState : gameStates) {
            if (gameState instanceof EntityState entityState) {
                updateEntityState(entityState);
            }
        }
        cleanupEntities(deltaTime);
    }

    private void updateEntityState(EntityState entityState) {
        TimedRenderableObject renderableObject;
        if (entities.containsKey(entityState.entityId())) {
            renderableObject = entities.get(entityState.entityId());
            renderableObject.setPosition(entityState.position());
            renderableObject.setVelocity(entityState.velocity());
        } else {
            renderableObject = renderableObjectFactory.createRenderableObject(entityState);
            entities.put(entityState.entityId(), renderableObject);
        }
        renderableObject.timeSinceUpdate = 0f;
    }
    private void cleanupEntities(float deltaTime)
    {
        for (Map.Entry<Integer, TimedRenderableObject> entry : entities.entrySet()) {
            TimedRenderableObject renderableObject = entry.getValue();
            renderableObject.timeSinceUpdate += deltaTime;
            if (renderableObject.timeSinceUpdate >= timeThreshold) {
                renderableObject.dispose();
                entities.remove(entry.getKey());
            }
        }
    }
}
