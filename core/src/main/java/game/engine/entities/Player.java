package game.engine.entities;

import game.actions.Direction;
import game.engine.PlayerConfig;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IMovingGeometryRepresentation;
import game.gamestates.EntityState;
import game.gamestates.PlayerState;
import game.utility.Rectangle2F;
import game.utility.Vector2F;

public class Player implements IMovingEntity {
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final SpriteID spriteID;
    private final IMovingGeometryRepresentation movingGeometryRepresentation;

    //should be from file or config
    private final float speed = 8f;
    private final Vector2F sightRange = new Vector2F(100, 100);

    public Player(PlayerConfig config, IMovingGeometryRepresentation movingGeometryRepresentation, int entityId) {
        this.movingGeometryRepresentation = movingGeometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = config.geometryConfigID();
        this.spriteID = config.spriteID();
    }
    @Override
    public void move(Direction direction) {
        movingGeometryRepresentation.move(direction.vector().multiply(speed));
    }
    @Override
    public EntityState getEntityState() {
        return new EntityState(
                entityId,
                geometryConfigID,
                movingGeometryRepresentation.getPosition(),
                movingGeometryRepresentation.getVelocity(),
                spriteID
        );
    }
    public PlayerState getPlayerState() {
        return new PlayerState(
                geometryConfigID,
                movingGeometryRepresentation.getPosition(),
                movingGeometryRepresentation.getVelocity(),
                spriteID
        );
    }
    public Rectangle2F getSightRange() {
        return new Rectangle2F(
                movingGeometryRepresentation.getPosition().subtract(sightRange),
                movingGeometryRepresentation.getPosition().add(sightRange)
        );
    }
    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return movingGeometryRepresentation;
    }

}
