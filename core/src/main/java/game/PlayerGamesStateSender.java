package game;

import game.gamestates.GameState;

public interface PlayerGamesStateSender {
    void sendGameState(GameState gameState);
}
