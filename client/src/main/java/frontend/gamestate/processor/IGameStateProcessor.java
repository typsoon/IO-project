package frontend.gamestate.processor;

import game.gamestates.IGameState;

import java.util.Collection;

public interface IGameStateProcessor {
    void processGameStates(Collection<IGameState> gameState, float deltaTime);
}
