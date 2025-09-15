package game.engine.entities;

import game.engine.PlayerConfig;
import game.engine.entities.behaviours.Wandering;
import game.engine.entities.concreteentities.BasicProjectile;
import game.engine.entities.concreteentities.Chicken;
import game.engine.entities.concreteentities.Player;
import game.engine.entities.geometry.EntityGeometryConfigFactory;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.entities.items.items.BasicArrow;
import game.engine.entities.items.items.BasicBow;
import game.engine.modules.IGeometryFactory;
import game.engine.modules.IManagingGeometryRepresentation;
import game.utility.Point2F;
import game.utility.Vector2F;

import java.util.function.Consumer;

public class EntityFactory {
    private final Incrementer entityId = new Incrementer(0);
    private final IGeometryFactory geometryFactory;

    public EntityFactory(IGeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    private Consumer<IEntity> entityCallback = entity -> {
    };
    private Consumer<IAIEntity> entityAICallback = entity -> {
    };
    private Consumer<IEntity> onDeath = entity -> {
    };

    public void setCallbacks(Consumer<IEntity> entityCallback, Consumer<IAIEntity> entityAICallback, Consumer<IEntity> onDeath) {
        this.entityCallback = entityCallback;
        this.entityAICallback = entityAICallback;
        this.onDeath = onDeath;
    }

    public Player createPlayer(PlayerConfig playerConfig, float startingX, float startingY) {
        Player player = new Player(playerConfig, geometryFactory.createGeometryRepresentation(
                EntityGeometryConfigFactory.createEntityGeometryConfig(GeometryConfigID.HUMAN),
                startingX, startingY), entityId.next(), onDeath);
        player.getInventory().getSlot(0).addItems(1, new BasicBow());
        player.getInventory().getSlot(1).addItems(15, new BasicArrow(
                (user, modifiers) -> {
                    IManagingGeometryRepresentation geometry = geometryFactory.createGeometryRepresentation(
                            EntityGeometryConfigFactory.createEntityGeometryConfig(GeometryConfigID.ARROW),
                            user.getEntityState().position().x(), user.getEntityState().position().y(), true);
                    BasicProjectile projectile = new BasicProjectile(
                            geometry,
                            entityId.next(), GeometryConfigID.ARROW, EntityGroupID.ARROW, onDeath,
                            user, modifiers
                    );
                    entityCallback.accept(projectile);
                    entityAICallback.accept(projectile);
                    geometry.setRotation(user.geometryRepresentation().getRotation());
                    geometry.setVelocity(new Vector2F(10f, 0f).rotate(user.geometryRepresentation().getRotation()));
                }
        ));
        entityCallback.accept(player);
        entityAICallback.accept(player);
        return player;
    }

    public IAIEntity createChicken(float startingX, float startingY) {
        Chicken chicken = new Chicken(geometryFactory.createGeometryRepresentation(
                EntityGeometryConfigFactory.createEntityGeometryConfig(GeometryConfigID.CHICKEN),
                startingX, startingY), entityId.next(), GeometryConfigID.CHICKEN, EntityGroupID.CHICKEN,
                new Wandering(new Point2F(startingX, startingY), 5.0f, 50.0f),
                onDeath);
        entityCallback.accept(chicken);
        entityAICallback.accept(chicken);
        return chicken;
    }

    private static class Incrementer {
        private int value;

        public Incrementer(int initialValue) {
            this.value = initialValue;
        }

        public int next() {
            return value++;
        }
    }
}
