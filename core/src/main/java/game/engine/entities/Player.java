package game.engine.entities;

import game.actions.Direction;
import game.actions.PlayerMove;
import game.engine.IWorldView;
import game.engine.PlayerConfig;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;
import game.gamestates.PlayerState;
import game.utility.Rectangle2F;
import game.utility.Vector2F;

public class Player implements IAIEntity {
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final MoveSet moveset = new MoveSet();

    // should be from file or config
    private final float speed = 8f;
    private final Vector2F sightRange = new Vector2F(30, 30);

    public Player(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, int entityId) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = config.geometryConfigID();
        this.entityGroupID = config.entityGroupID();
        moveset.move = new PlayerMove(Direction.NONE);
    }

    public MoveSet getMoveSet(){
        return moveset;
    }

    @Override
    public void think(IWorldView view) {
        move(moveset.move.direction());
    }

    private void move(Direction direction) {
        geometryRepresentation.move(direction.vector().multiply(speed));
    }

    @Override
    public EntityState getEntityState() {
        return new EntityState(
                entityId,
                geometryConfigID,
                geometryRepresentation.getPosition(),
                geometryRepresentation.getVelocity(),
                geometryRepresentation.getRotation(),
                entityGroupID,
                EntityAction.Idle);
    }

    public PlayerState getPlayerState() {
        return new PlayerState(
                entityId,
                geometryConfigID,
                geometryRepresentation.getPosition(),
                geometryRepresentation.getVelocity(),
                geometryRepresentation.getRotation(),
                entityGroupID,
                EntityAction.Idle,
                sightRange);
    }

    public Rectangle2F getSightRange() {
        return new Rectangle2F(
                geometryRepresentation.getPosition().subtract(sightRange),
                geometryRepresentation.getPosition().add(sightRange));
    }

    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return geometryRepresentation;
    }

}
