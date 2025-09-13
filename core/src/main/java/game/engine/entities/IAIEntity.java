package game.engine.entities;

import game.engine.IWorldView;

//todo goofy looking name
public interface IAIEntity extends IEntity {
    void think(IWorldView view);
}
