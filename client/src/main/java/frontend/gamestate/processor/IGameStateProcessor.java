package frontend.gamestate.processor;

import game.gamestates.IGameState;

public interface IGameStateProcessor {
    void processGameState(IGameState gameState);
}
