package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.utility.Vector2F;

public interface IPlayerData {
    int getHpValue();

    int getMaxHpValue();

    DrawableInfo getDrawableInfo();

    Vector2F getRange();

//    InventoryInfo getInventoryInfo();
}
