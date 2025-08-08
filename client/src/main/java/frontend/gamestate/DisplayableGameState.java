package frontend.gamestate;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Collection;

public class DisplayableGameState implements IDisplayableGameState{
    Collection<Sprite> sprites;
    int hpValue;
    int maxHpValue;
    public DisplayableGameState(){
        sprites = new java.util.ArrayList<>(); //ToDo: use a more specific collection type if needed
        hpValue = 0;
        maxHpValue = 0;
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
    public int getHpValue() {
        return hpValue;
    }
    @Override
    public void SetHpValue(int hpValue) {
        if (hpValue >= 0) {
            this.hpValue = hpValue;
        }
    }
    @Override
    public int getMaxHpValue() {
        return maxHpValue;
    }
    @Override
    public void SetMaxHpValue(int maxHpValue) {
        if (maxHpValue >= hpValue) {
            this.maxHpValue = maxHpValue;
        }
    }
}
