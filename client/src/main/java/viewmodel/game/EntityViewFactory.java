package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.gamestates.EntityState;

// Placeholder for EntityViewFactory or something better
public class EntityViewFactory {
    private final SpriteBatch batch;

    public EntityViewFactory(SpriteBatch batch) {
        this.batch = batch;
    }

    public EntityView createEntityView(EntityState entityState) {
        return null;
    }
}
