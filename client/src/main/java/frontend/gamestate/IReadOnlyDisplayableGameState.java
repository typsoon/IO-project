package frontend.gamestate;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Collection;

public interface IReadOnlyDisplayableGameState {
    Collection<Sprite> getSpritesReadonly();

    int getHpValue();
    int getMaxHpValue();
    //TODO: add more getters for other game state properties
}
