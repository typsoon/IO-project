package viewmodel.game;

import game.gamestates.EntityState;

public interface EntityViewFactory {
    EntityView createEntityView(EntityState entityState);
}
