package game;

import game.gamestates.GameState;

import java.util.Collection;

public interface PlayerConnector {
    void subscribe(ActionReceiver receiver);
    void unsubscribe(ActionReceiver receiver);
    void sendGameState(Collection<GameState> gameStates);
}
