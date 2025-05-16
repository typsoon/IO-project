package game;

import java.util.Collection;

public interface PlayerConnector {
    void subscribe(ActionReceiver receiver);
    void sendGameState(Collection<GameState> gameStates);
}
