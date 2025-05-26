package game.engine.entities;

import game.actions.Direction;

public interface IMovingEntity extends IEntity {
    void move(Direction direction);
}
