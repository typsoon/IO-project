package game.session;

import game.gamestates.IGameState;

public interface IPlayerGamesStateSender {
    void sendGameState(IGameState gameState);
}
