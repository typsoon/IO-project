package game.session;

import game.gamestates.IGameState;

import java.util.Collection;

public interface IPlayerConnector {
    void subscribe(IActionReceiver receiver);

    void unsubscribe(IActionReceiver receiver);

    void sendGameState(Collection<IGameState> gameStates);
}
