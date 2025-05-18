package game.session;

import game.PlayerConnector;
import game.PlayerData;
import game.PlayerGamesStateSender;
import game.engine.EnginePlayerData;
import game.engine.GameEngine;
import game.engine.GameEngineFactory;

import java.util.Collection;
import java.util.Map;

public class GameSessionFactory {
    public static GameSessionManager createGameSessionManager(Collection<PlayerData> players) {
        GameSessionManager gameSessionManager = new GameSessionManager(players);
        Map<PlayerConnector, PlayerGamesStateSender> playerGameStateSenders = gameSessionManager.getPlayerGameStateSenders();
        Collection<EnginePlayerData> enginePlayerData = players.stream()
                .map(player -> new EnginePlayerData(playerGameStateSenders.get(player.connector()),player.config()))
                .toList();
        GameEngine gameEngine = GameEngineFactory.createGameEngine(enginePlayerData);
        gameSessionManager.SetupEngine(gameEngine);
        return gameSessionManager;
    }
}
