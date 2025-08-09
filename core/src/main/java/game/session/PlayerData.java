package game.session;

import java.util.Collection;

import game.engine.PlayerConfig;
import game.gamestates.IGameState;

public record PlayerData(ISubscribablePlayerConnector connector,
        PlayerConfig config) {

    public PlayerData(IPlayerConnector connector, PlayerConfig config) {
        this(new ISubscribablePlayerConnector() {

            @Override
            public void subscribe(IActionReceiver receiver) {
            }

            @Override
            public void unsubscribe(IActionReceiver receiver) {
            }

            @Override
            public void sendGameStates(Collection<IGameState> gameStates) {
                connector.sendGameStates(gameStates);
            }
        }, config);
    }
}
