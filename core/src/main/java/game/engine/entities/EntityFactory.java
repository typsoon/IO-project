package game.engine.entities;

import game.engine.PlayerConfig;
import game.engine.entities.behaviours.Wandering;
import game.engine.modules.IGeometryFactory;
import game.utility.Point2F;

public class EntityFactory {
    private final Incrementer entityId = new Incrementer(0);
    private final IGeometryFactory geometryFactory;
    public EntityFactory(IGeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }
    public Player createPlayer(PlayerConfig playerConfig, float startingX, float startingY) {
        return new Player(playerConfig,geometryFactory.createGeometryRepresentation(
                EntityGeometryConfigFactory.createEntityGeometryConfig(GeometryConfigID.HUMAN),
                startingX, startingY), entityId.next());
    }
    public IAIEntity createChicken(float startingX, float startingY) {
        return new Chicken(geometryFactory.createGeometryRepresentation(
                EntityGeometryConfigFactory.createEntityGeometryConfig(GeometryConfigID.CHICKEN),
                startingX, startingY), entityId.next(),GeometryConfigID.CHICKEN,EntityGroupID.CHICKEN,
                new Wandering(new Point2F(startingX, startingY),5.0f, 50.0f));
    }
    private static class Incrementer{
        private int value;

        public Incrementer(int initialValue) {
            this.value = initialValue;
        }

        public int next() {
            return value++;
        }
    }
}
