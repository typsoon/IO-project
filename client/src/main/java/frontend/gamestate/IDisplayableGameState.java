package frontend.gamestate;

import viewmodel.game.IPlayerData;

public interface IDisplayableGameState extends IReadOnlyDisplayableGameState {
    void AddDrawable(DrawableInfo drawable);
    void RemoveDrawable(DrawableInfo drawableInfo);
    void AddPlayer(IPlayerData player);
    void RemovePlayer(IPlayerData player);
}
