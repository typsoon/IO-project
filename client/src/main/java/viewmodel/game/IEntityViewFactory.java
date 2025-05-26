package viewmodel.game;

import game.gamestates.EntityState;

public interface IEntityViewFactory {
    IEntityView createEntityView(EntityState entityState);
}
