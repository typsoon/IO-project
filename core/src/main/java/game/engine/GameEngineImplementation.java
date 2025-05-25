package game.engine;

import game.engine.entities.Entity;
import game.engine.modules.GeometryRepresentation;
import game.session.PlayerGamesStateSender;
import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.engine.entities.EntityFactory;
import game.engine.modules.GeometryModule;
import game.engine.entities.Player;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GameEngineImplementation implements GameEngine {

    private final GeometryModule geometryModule;
    private final EntityFactory entityFactory;
    private final Map<PlayerGamesStateSender, Player> players = new java.util.HashMap<>();
    private final Map<GeometryRepresentation, Entity> entities = new java.util.HashMap<>();
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
        geometryModule.Cycle();

        for (Map.Entry<PlayerGamesStateSender, Player> entry : players.entrySet()) {
            PlayerGamesStateSender sender = entry.getKey();
            Player player = entry.getValue();
            sender.sendGameState(player.getPlayerState());
            for(GeometryRepresentation geometryRepresentation: geometryModule.getEntitiesInArea(player.getSightRange())){
                if(geometryRepresentation == player.geometryRepresentation())
                    continue; // Skip sending the player's own state
                sender.sendGameState(entities.get(geometryRepresentation).getEntityState());
            }
        }
    }

    protected GameEngineImplementation(Collection<EnginePlayerData> players, GeometryModule geometryModule, EntityFactory entityFactory, Collection<Closeable> resourcesToClose) {
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
    public GeometryModule getGeometryModule() {
        return geometryModule;
    }
}
