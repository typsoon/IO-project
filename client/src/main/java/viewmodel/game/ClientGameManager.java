package viewmodel.game;

import game.gamestates.GameState;

import java.util.Collection;

public interface ClientGameManager {
    void updateState(Collection<GameState> gameStates);

    void performCycle();
}
