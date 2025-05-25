package game.engine.entities;

import game.actions.Direction;

public interface MovingEntity extends Entity {
    void move(Direction direction);
}
