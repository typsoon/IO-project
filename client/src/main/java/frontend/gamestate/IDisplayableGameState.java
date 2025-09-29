package frontend.gamestate;

import viewmodel.game.IPlayerData;

public interface IDisplayableGameState extends IReadOnlyDisplayableGameState {
    void addDrawable(DrawableInfo drawable);

    void removeDrawable(DrawableInfo drawableInfo);

    void addPlayer(IPlayerData player);

    void removePlayer(IPlayerData player);

    OverlaysData getOverlaysData();
}
