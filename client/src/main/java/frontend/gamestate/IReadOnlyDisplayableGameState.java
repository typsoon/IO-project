package frontend.gamestate;

import java.util.Collection;

import viewmodel.game.IPlayerData;

public interface IReadOnlyDisplayableGameState {
    Collection<DrawableInfo> getSpritesReadonly();

    Collection<IPlayerData> getPlayerData();

    IReadOnlyOverlaysData getOverlaysData();
}
