package frontend.gamestate;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface IDisplayableGameState extends IReadOnlyDisplayableGameState {
    void AddSprite(Sprite sprite);
    void RemoveSprite(Sprite sprite);
}
