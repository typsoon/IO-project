package viewmodel.game;

import game.gamestates.IGameState;

import java.util.Collection;

public interface IClientGameManager {
    void updateState(Collection<IGameState> gameStates);

    void performCycle();
}
