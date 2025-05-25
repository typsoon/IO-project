package viewmodel.game;

import game.engine.modules.*;
import game.gamestates.EntityState;
import game.gamestates.GameState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


// not finished yet, but for now I have no idea how the interface should look like
public class ClientGameManagerImplementation implements ClientGameManager {
    private final GeometrySystem geometrySystem;
    private final Map<Integer, EntityView> entityViews = new HashMap<>();
    private final Map<Integer, Integer> timeSinceLastUpdate = new HashMap<>();
    private final int updateThreshold = 10;
    private final EntityViewFactory entityViewFactory;

    ClientGameManagerImplementation(GeometrySystem geometrySystem, EntityViewFactory entityViewFactory) {
        this.geometrySystem = geometrySystem;
        this.entityViewFactory = entityViewFactory;
    }

    @Override
    public void updateState(Collection<GameState> gameStates) {
        for (GameState gameState : gameStates) {
            if (gameState instanceof EntityState entityState) {
                updateEntityState(entityState);
            }
        }
    }

    void updateEntityState(EntityState entityState) {
        if (entityViews.containsKey(entityState.entityId())) {
            EntityView entityView = entityViews.get(entityState.entityId());
            entityView.setPosition(entityState.position());
            entityView.setVelocity(entityState.velocity());
            timeSinceLastUpdate.put(entityState.entityId(), 0);
        }
        else {
            EntityView entityView = entityViewFactory.createEntityView(entityState);
            entityViews.put(entityState.entityId(), entityView);
            timeSinceLastUpdate.put(entityState.entityId(), 0);
        }
    }

    @Override
    public void performCycle() {
        geometrySystem.Cycle();
        for (Map.Entry<Integer, Integer> entry : timeSinceLastUpdate.entrySet()) {
            int entityId = entry.getKey();
            int time = entry.getValue() + 1;
            if (time >= updateThreshold) {

                EntityView entityView = entityViews.get(entityId);
                entityView.dispose();
                entityViews.remove(entityId);
                timeSinceLastUpdate.remove(entityId);
                continue;
            }
            timeSinceLastUpdate.put(entityId, time);
            EntityView entityView = entityViews.get(entityId);
        }
    }

}
