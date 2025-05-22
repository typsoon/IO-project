package game.engine.components;

import game.actions.Direction;
import game.engine.PlayerConfig;

public class Player implements Entity {

    private final GeometryRepresentation geometryRepresentation;

    public Player(PlayerConfig config, GeometryRepresentation geometryRepresentation) {
        this.geometryRepresentation = geometryRepresentation;
    }
    public void move(Direction direction) {
        geometryRepresentation.move(direction.vector());
    }
}
