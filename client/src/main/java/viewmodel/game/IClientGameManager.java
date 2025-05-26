package viewmodel.game;

import game.gamestates.GameState;

import java.util.Collection;

public interface IClientGameManager {
    void updateState(Collection<GameState> gameStates);

    void performCycle();
}
