package game.engine;

import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.engine.entities.EntityFactory;
import game.engine.entities.IAIEntity;
import game.engine.entities.IEntity;
import game.engine.entities.MoveSet;
import game.engine.entities.concreteentities.Player;
import game.engine.modules.IGeometryModule;
import game.engine.modules.IGeometryRepresentation;
import game.session.IPlayerGamesStateSender;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GameEngine implements IGameEngine, IWorldView {

    private final IGeometryModule geometryModule;
    private final EntityFactory entityFactory;
    private final Map<IPlayerGamesStateSender, Player> players = new java.util.HashMap<>();
    private final Map<IGeometryRepresentation, IEntity> entities = new java.util.HashMap<>();
    private final Collection<Closeable> resourcesToClose;

    private final Collection<IAIEntity> thinkers = new java.util.ArrayList<>();

    @Override
    public void performCycle(Collection<Event> events) {
        for (Event event : events) {
            Player player = players.get(event.playerGamesStateSender());

            if (player != null) {
                MoveSet moveSet = player.getMoveSet();
                switch (event.action()) {
                    case PlayerMove playerMove -> moveSet.move = playerMove;
                    case PlayerSlotUse playerSlotUse -> moveSet.slotUse = playerSlotUse;
                    default -> throw new IllegalArgumentException("Unknown action: " + event.action());
                }
            }
        }
        // this is quick fix
        Collection<IAIEntity> thinkersCopy = new java.util.ArrayList<>(thinkers);
        thinkersCopy.forEach(thinker -> thinker.think(this));
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

        // probably should be done by a decorator ObservableEntityFactory but building
        // process would be complex
        entityFactory.setCallbacks(
                entity -> entities.put(entity.geometryRepresentation(), entity),
                thinkers::add,
                entity -> {
                    entities.remove(entity.geometryRepresentation());
                    if (entity instanceof IAIEntity aiEntity) {
                        thinkers.remove(aiEntity);
                    }
                });

        for (EnginePlayerData playerData : players) {
            Player player = entityFactory.createPlayer(playerData.playerConfig(), 0, 0);
            this.players.put(playerData.playerGamesStateSender(), player);
        }

        entityFactory.createChicken(0, 0);
        entityFactory.createChicken(0, 0);
        entityFactory.createChicken(0, 0);
        entityFactory.createChicken(0, 0);
        entityFactory.createChicken(0, 0);

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
