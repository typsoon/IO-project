package game.engine;

import game.engine.entities.*;
import game.engine.modules.IGeometryRepresentation;
import game.session.IPlayerGamesStateSender;
import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.engine.modules.IGeometryModule;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import game.engine.entities.IAIEntity;

public class GameEngine implements IGameEngine, IWorldView {

    private final IGeometryModule geometryModule;
    private final EntityFactory entityFactory;
    private final Map<IPlayerGamesStateSender, Player> players = new java.util.HashMap<>();
    private final Map<IGeometryRepresentation, IEntity> entities = new java.util.HashMap<>();
    private final Collection<Closeable> resourcesToClose;

    private final IAIEntity chicken;

    @Override
    public void performCycle(Collection<Event> events) {
        for (Event event : events) {
            Player player = players.get(event.playerGamesStateSender());
            if (player != null) {
                switch (event.action()) {
                    case PlayerMove playerMove -> player.move(playerMove.direction());
                    case PlayerSlotUse playerSlotUse ->
                        throw new UnsupportedOperationException("PlayerSlotUse handling not implemented");
                    default -> throw new IllegalArgumentException("Unknown action: " + event.action());
                }
            }
        }
        chicken.think(this);
        geometryModule.cycle();

        for (Map.Entry<IPlayerGamesStateSender, Player> entry : players.entrySet()) {
            IPlayerGamesStateSender sender = entry.getKey();
            Player player = entry.getValue();
            sender.sendGameState(player.getPlayerState());
            for (IGeometryRepresentation geometryRepresentation : geometryModule
                    .getEntitiesInArea(player.getSightRange())) {
                if (geometryRepresentation == player.geometryRepresentation())
                    continue; // Skip sending the player's own state
                sender.sendGameState(entities.get(geometryRepresentation).getEntityState());
            }
        }
    }

    public Collection<IEntity> getEntitiesInArea(float x, float y, float width, float height) {
        return geometryModule.getEntitiesInArea(x, y, width, height).stream()
                .map(entities::get)
                .toList();
    }

    protected GameEngine(Collection<EnginePlayerData> players, IGeometryModule geometryModule,
            EntityFactory entityFactory, Collection<Closeable> resourcesToClose) {

        this.geometryModule = geometryModule;
        this.entityFactory = entityFactory;
        this.resourcesToClose = resourcesToClose;
        for (EnginePlayerData playerData : players) {
            Player player = entityFactory.createPlayer(playerData.playerConfig(), 0, 0);
            entities.put(player.geometryRepresentation(), player);
            this.players.put(playerData.playerGamesStateSender(), player);
        }
        chicken = entityFactory.createChicken(0, 0);
        entities.put(chicken.geometryRepresentation(), chicken);

    }

    @Override
    public void close() throws IOException {
        for (Closeable closeable : resourcesToClose) {
            closeable.close();
        }
    }

    // for testing purposes
    public IGeometryModule getGeometryModule() {
        return geometryModule;
    }
}
