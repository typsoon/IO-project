package frontend.gamestate;

import viewmodel.game.IPlayerData;

import java.util.Collection;

public interface IReadOnlyDisplayableGameState {
    Collection<DrawableInfo> getSpritesReadonly();
    Collection<IPlayerData> getPlayerData();
}
