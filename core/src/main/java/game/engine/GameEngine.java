package game.engine;

import game.engine.entities.IEntity;
import game.engine.modules.IGeometryRepresentation;
import game.session.IPlayerGamesStateSender;
import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.engine.entities.EntityFactory;
import game.engine.modules.IGeometryModule;
import game.engine.entities.Player;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GameEngine implements IGameEngine {

    private final IGeometryModule geometryModule;
    private final EntityFactory entityFactory;
    private final Map<IPlayerGamesStateSender, Player> players = new java.util.HashMap<>();
    private final Map<IGeometryRepresentation, IEntity> entities = new java.util.HashMap<>();
    private final Collection<Closeable> resourcesToClose;

    @Override
    public void PerformCycle(Collection<Event> events) {
        for (Event event : events) {
            Player player = players.get(event.playerGamesStateSender());
            if (player != null) {
                switch (event.action()){
                    case PlayerMove playerMove -> player.move(playerMove.direction());
                    case PlayerSlotUse playerSlotUse -> throw new UnsupportedOperationException("PlayerSlotUse handling not implemented");
                    default -> throw new IllegalArgumentException("Unknown action: " + event.action());
                }
            }
        }
        geometryModule.cycle();

        for (Map.Entry<IPlayerGamesStateSender, Player> entry : players.entrySet()) {
            IPlayerGamesStateSender sender = entry.getKey();
            Player player = entry.getValue();
            sender.sendGameState(player.getPlayerState());
            for(IGeometryRepresentation geometryRepresentation: geometryModule.getEntitiesInArea(player.getSightRange())){
                if(geometryRepresentation == player.geometryRepresentation())
                    continue; // Skip sending the player's own state
                sender.sendGameState(entities.get(geometryRepresentation).getEntityState());
            }
        }
    }

    protected GameEngine(Collection<EnginePlayerData> players, IGeometryModule geometryModule, EntityFactory entityFactory, Collection<Closeable> resourcesToClose) {
        this.geometryModule = geometryModule;
        this.entityFactory = entityFactory;
        this.resourcesToClose = resourcesToClose;
        for (EnginePlayerData playerData : players) {
            Player player = entityFactory.createPlayer(playerData.playerConfig(),0,0);
            entities.put(player.geometryRepresentation(), player);
            this.players.put(playerData.playerGamesStateSender(), player);
        }
    }
    @Override
    public void close() throws IOException{
        for (Closeable closeable : resourcesToClose) {
                closeable.close();
        }
    }

    //for testing purposes
    public IGeometryModule getGeometryModule() {
        return geometryModule;
    }
}
