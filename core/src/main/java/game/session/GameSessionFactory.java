package game.session;

import game.engine.EnginePlayerData;
import game.engine.IGameEngine;
import game.engine.GameEngineFactory;

import java.util.Collection;
import java.util.Map;

public class GameSessionFactory {
    public static GameSessionManager createGameSessionManager(Collection<PlayerData> players) {
        GameSessionManager gameSessionManager = new GameSessionManager(players);
        Map<ISubscribablePlayerConnector, IPlayerGamesStateSender> playerGameStateSenders = gameSessionManager
                .getPlayerGameStateSenders();
        Collection<EnginePlayerData> enginePlayerData = players.stream()
                .map(player -> new EnginePlayerData(playerGameStateSenders.get(player.connector()), player.config()))
                .toList();
        IGameEngine gameEngine = GameEngineFactory.createGameEngine(enginePlayerData);
        gameSessionManager.setupEngine(gameEngine);
        return gameSessionManager;
    }
}
