package viewmodel.game;

import frontend.gamestate.DrawableInfo;

public interface IPlayerData {
    int getHpValue();
    int getMaxHpValue();
    DrawableInfo getDrawableInfo();
}
