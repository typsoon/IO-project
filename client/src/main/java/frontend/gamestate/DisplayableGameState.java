package frontend.gamestate;

import com.badlogic.gdx.graphics.g2d.Sprite;
import viewmodel.game.IPlayerData;

import java.util.Collection;

public class DisplayableGameState implements IDisplayableGameState{
    Collection<Sprite> sprites;
    IPlayerData playerData;
    public DisplayableGameState(IPlayerData playerData) {
        sprites = new java.util.ArrayList<>(); //ToDo: use a more specific collection type if needed
        this.playerData = playerData;
    }
    @Override
    public Collection<Sprite> getSpritesReadonly() {
        return java.util.Collections.unmodifiableCollection(sprites);
    }

    @Override
    public void AddSprite(Sprite sprite) {
        if (sprite != null) {
            sprites.add(sprite);
        }
    }
    @Override
    public void RemoveSprite(Sprite sprite) {
        if (sprite != null) {
            sprites.remove(sprite);
        }
    }
    @Override
    public IPlayerData getPlayerData() {
        return playerData;
    }
}
