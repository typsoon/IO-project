package game.session;

import game.engine.PlayerConfig;
import game.gamestates.IGameState;

import java.util.Collection;

public record PlayerData(ISubscribablePlayerConnector connector,
                         PlayerConfig config) {

    private record SubscribablePlayerConnectorImpl(IPlayerConnector playerConnector)
            implements ISubscribablePlayerConnector {
        @Override
        public void sendGameStates(Collection<IGameState> gameStates) {
            playerConnector.sendGameStates(gameStates);
        }

        @Override
        public void subscribe(IActionReceiver receiver) {
        }

        @Override
        public void unsubscribe(IActionReceiver receiver) {
        }
    }

    public PlayerData(IPlayerConnector connector, PlayerConfig config) {
        this(new SubscribablePlayerConnectorImpl(connector), config);
    }
}
