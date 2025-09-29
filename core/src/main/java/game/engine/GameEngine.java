package game.engine;

import game.actions.IInteraction;
import game.actions.PlayerInteraction;
import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.engine.entities.*;
import game.engine.entities.concreteentities.Player;
import game.engine.entities.geometry.ICollisionAware;
import game.engine.modules.ICollisionSubscriber;
import game.engine.modules.IGeometryModule;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.PlayerDeathState;
import game.session.IPlayerGamesStateSender;
import utils.IDisposable;

import java.util.Collection;
import java.util.Map;

public class GameEngine implements IGameEngine, IWorldView, ICollisionSubscriber {

    private final IGeometryModule geometryModule;
    private final EntityFactory entityFactory;
    private final Map<IPlayerGamesStateSender, Player> players = new java.util.HashMap<>();
    private final Map<IGeometryRepresentation, IEntity> entities = new java.util.HashMap<>();
    private final Collection<IDisposable> resourcesToClose;

    private final Collection<IAIEntity> thinkers = new java.util.ArrayList<>();

    private final Collection<DeathData> toDispose = new java.util.HashSet<>();

    @Override
    public void performCycle(Collection<Event> events) {
        for (Event event : events) {
            Player player = players.get(event.playerGamesStateSender());

            if (player != null) {
                MoveSet moveSet = player.getMoveSet();
                switch (event.action()) {
                    case PlayerMove playerMove -> moveSet.move = playerMove;
                    case PlayerSlotUse playerSlotUse -> moveSet.slotUse = playerSlotUse;
                    case PlayerInteraction playerInteraction -> moveSet.interaction = playerInteraction;
                    case IInteraction interaction -> {
                        if (player.isInteracting()) {
                            player.getCurrentInteraction().interact(interaction, player);
                        }
                    }
                    default -> throw new IllegalArgumentException("Unknown action: " + event.action());
                }
            }
        }
        gameLoop();

        sendGameStates();
    }

    private void sendGameStates() {
        for (Map.Entry<IPlayerGamesStateSender, Player> entry : players.entrySet()) {
            IPlayerGamesStateSender sender = entry.getKey();
            Player player = entry.getValue();
            sender.sendGameState(player.getPlayerState());
            for (IGeometryRepresentation geometryRepresentation : geometryModule.getEntitiesInArea(player.getSightRange())) {
                if (geometryRepresentation == player.geometryRepresentation())
                    continue; // Skip sending the player's own state
                sender.sendGameState(entities.get(geometryRepresentation).getEntityState());
            }

            sender.sendGameState(player.getPlayerInventoryState());

            if (player.isInteracting()) {
                sender.sendGameState(player.getCurrentInteraction().getState());
            }

            if (player.getUpgradeTreeState() != null) {
                sender.sendGameState(player.getUpgradeTreeState());
            }
        }
    }

    private void gameLoop() {
        Collection<IAIEntity> thinkersCopy = new java.util.ArrayList<>(thinkers);
        thinkersCopy.forEach(thinker -> thinker.think(this));

        geometryModule.cycle();

        toDispose.forEach(deathData -> {
            IEntity entity = deathData.entity();
            if (entity instanceof IAIEntity iaiEntity) {
                thinkers.remove(iaiEntity);
            }
            if (entity instanceof Player player) {
                var it = players.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<IPlayerGamesStateSender, Player> entry = it.next();
                    if (entry.getValue() == player) {
                        entry.getKey().sendGameState(new PlayerDeathState());
                        it.remove();
                        break;
                    }
                }
            }
            entities.remove(entity.geometryRepresentation());
            deathData.geometryRepresentation().dispose();
        });
    }

    public Collection<IEntity> getEntitiesInArea(float x, float y, float width, float height) {
        return geometryModule.getEntitiesInArea(x, y, width, height).stream()
                .map(entities::get)
                .toList();
    }

    protected GameEngine(Collection<EnginePlayerData> players, IGeometryModule geometryModule,
                         EntityFactory entityFactory, Collection<IDisposable> resourcesToClose) {

        this.geometryModule = geometryModule;
        geometryModule.subscribeToCollisions(this);
        this.entityFactory = entityFactory;
        this.resourcesToClose = resourcesToClose;

        // probably should be done by a decorator ObservableEntityFactory but building
        // process would be complex
        entityFactory.setCallbacks(
                entity -> entities.put(entity.geometryRepresentation(), entity),
                thinkers::add,
                toDispose::add
        );

        for (EnginePlayerData playerData : players) {
            Player player = entityFactory.createPlayer(playerData.playerConfig(), 0, 0);
            this.players.put(playerData.playerGamesStateSender(), player);
        }

        createEnvironment();
    }

    private void createEnvironment() {
        int count = 10;
        while (count-- > 0) {
            entityFactory.createChicken(0, 0);
        }
        entityFactory.createStump(5, 5);
        entityFactory.createRock(-5, -5);

        float worldSize = 15f;          //TODO: should be more global and tied to geometryConfig of border
        float borderSize = 60f;         //the same as in assets/geometryconfigs/border.yaml
        //border east
        entityFactory.createBorder(worldSize + borderSize / 2, -worldSize + borderSize / 2);
        //border north
        entityFactory.createBorder(worldSize - borderSize / 2, worldSize + borderSize / 2);
        //border west
        entityFactory.createBorder(-worldSize - borderSize / 2, worldSize - borderSize / 2);
        //border south
        entityFactory.createBorder(-worldSize + borderSize / 2, -worldSize - borderSize / 2);
    }

    @Override
    public void dispose() {
        for (IDisposable disposable : resourcesToClose) {
            disposable.dispose();
        }
    }

    // for testing purposes
    public IGeometryModule getGeometryModule() {
        return geometryModule;
    }

    @Override
    public void onCollisionBegin(IManagingGeometryRepresentation entityA, IManagingGeometryRepresentation entityB) {
        IEntity a = entities.get(entityA);
        IEntity b = entities.get(entityB);
        if (a instanceof ICollisionAware collisionAware) {
            collisionAware.onCollisionBegin(b);
        }
        if (b instanceof ICollisionAware collisionAware) {
            collisionAware.onCollisionBegin(a);
        }
    }

    @Override
    public void onCollisionEnd(IManagingGeometryRepresentation entityA, IManagingGeometryRepresentation entityB) {

    }
}
