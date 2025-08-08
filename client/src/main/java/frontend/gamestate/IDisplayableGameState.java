package frontend.gamestate;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface IDisplayableGameState extends IReadOnlyDisplayableGameState {
    void AddSprite(Sprite sprite);
    void RemoveSprite(Sprite sprite);

    void SetHpValue(int hpValue);
    void SetMaxHpValue(int maxHpValue);
    //TODO: add more setters for other game state properties

}
