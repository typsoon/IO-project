package game.engine;

import game.PlayerGamesStateSender;
import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.engine.components.EntityFactory;
import game.engine.components.GeometryModule;
import game.engine.components.Player;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GameEngineImplementation implements GameEngine {

    private final GeometryModule geometryModule;
    private final EntityFactory entityFactory;
    private final Map<PlayerGamesStateSender, Player> players = new java.util.HashMap<>();
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
        //ToDO: Handle game state updates and send them to players
    }

    protected GameEngineImplementation(Collection<EnginePlayerData> players, GeometryModule geometryModule, EntityFactory entityFactory, Collection<Closeable> resourcesToClose) {
        this.geometryModule = geometryModule;
        this.entityFactory = entityFactory;
        this.resourcesToClose = resourcesToClose;
        for (EnginePlayerData playerData : players) {
            Player player = entityFactory.createPlayer(playerData.playerConfig());
            this.players.put(playerData.playerGamesStateSender(), player);
        }
    }
    @Override
    public void close() throws IOException{
        for (Closeable closeable : resourcesToClose) {
                closeable.close();
        }
    }
}
