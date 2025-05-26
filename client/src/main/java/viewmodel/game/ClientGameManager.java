package viewmodel.game;

import game.engine.modules.*;
import game.gamestates.EntityState;
import game.gamestates.IGameState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


// not finished yet, but for now I have no idea how the interface should look like
public class ClientGameManager implements IClientGameManager {
    private final IGeometryModule geometryModule;
    private final Map<Integer, IEntityView> entityViews = new HashMap<>();
    private final Map<Integer, Integer> timeSinceLastUpdate = new HashMap<>();
    private static final int updateThreshold = 10;
    private final IEntityViewFactory entityViewFactory;

    ClientGameManager(IGeometryModule geometryModule, IEntityViewFactory entityViewFactory) {
        this.geometryModule = geometryModule;
        this.entityViewFactory = entityViewFactory;
    }

    @Override
    public void updateState(Collection<IGameState> gameStates) {
        for (IGameState gameState : gameStates) {
            if (gameState instanceof EntityState entityState) {
                updateEntityState(entityState);
            }
        }
    }

    void updateEntityState(EntityState entityState) {
        IEntityView entityView;
        if (entityViews.containsKey(entityState.entityId())) {
            entityView = entityViews.get(entityState.entityId());
            entityView.setPosition(entityState.position());
            entityView.setVelocity(entityState.velocity());
        }
        else {
            entityView = entityViewFactory.createEntityView(entityState);
            entityViews.put(entityState.entityId(), entityView);
        }
        timeSinceLastUpdate.put(entityState.entityId(), 0);
    }

    @Override
    public void performCycle() {
        geometryModule.Cycle();
        for (Map.Entry<Integer, Integer> entry : timeSinceLastUpdate.entrySet()) {
            int entityId = entry.getKey();
            int time = entry.getValue() + 1;
            if (time >= updateThreshold) {

                IEntityView entityView = entityViews.get(entityId);
                entityView.dispose();
                entityViews.remove(entityId);
                timeSinceLastUpdate.remove(entityId);
                continue;
            }
            timeSinceLastUpdate.put(entityId, time);
            IEntityView entityView = entityViews.get(entityId);
        }
    }

}
