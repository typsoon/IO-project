package frontend.gamestate;

import com.badlogic.gdx.graphics.g2d.Sprite;
import viewmodel.game.IPlayerData;

import java.util.Collection;

public interface IReadOnlyDisplayableGameState {
    Collection<Sprite> getSpritesReadonly();
    IPlayerData getPlayerData();
}
