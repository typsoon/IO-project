package viewmodel.game;

import game.engine.modules.GeometryFactory;
import game.engine.modules.GeometryModule;
import game.engine.modules.GeometryModuleImplementation;
import game.engine.modules.ManagingGeometryRepresentation;
import game.gamestates.EntityState;
import game.gamestates.GameState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


// not finished yet, but for now I have no idea how the interface should look like
public class ClientGameManagerImplementation implements ClientGameManager {
    private final GeometryModuleImplementation geometryModule;
    private final Map<Integer, ManagingGeometryRepresentation> geometryRepresentations = new HashMap<>();
    private final Map<Integer, Integer> timeSinceLastUpdate = new HashMap<>();
    private final int updateThreshold = 10;

    ClientGameManagerImplementation(GeometryModuleImplementation geometryModule) {
        this.geometryModule = geometryModule;
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
        if (geometryRepresentations.containsKey(entityState.entityId())) {
            ManagingGeometryRepresentation representation = geometryRepresentations.get(entityState.entityId());
            representation.setPosition(entityState.position());
            representation.setVelocity(entityState.velocity());
            timeSinceLastUpdate.put(entityState.entityId(), 0);
        }
        else {
            ManagingGeometryRepresentation representation = geometryModule.createGeometryRepresentation(
                    EntityFactory.createEntityGeometryConfig(entityState.geometryConfigId()),
                    entityState.position().x(),
                    entityState.position().y()
            );
            geometryRepresentations.put(entityState.entityId(), representation);
            timeSinceLastUpdate.put(entityState.entityId(), 0);
        }
    }

    @Override
    public void performCycle() {
        geometryModule.Cycle();
        for (Map.Entry<Integer, Integer> entry : timeSinceLastUpdate.entrySet()) {
            int entityId = entry.getKey();
            int time = entry.getValue() + 1;
            if (time >= updateThreshold) {

                ManagingGeometryRepresentation representation = geometryRepresentations.get(entityId);
                representation.dispose();
                geometryRepresentations.remove(entityId);
                timeSinceLastUpdate.remove(entityId);
                continue;
            }
            timeSinceLastUpdate.put(entityId, time);
        }
    }

}
