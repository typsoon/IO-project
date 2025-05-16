package game;

import game.engine.DummyGameEngine;

import java.util.Collection;

public class GameSessionFactory {
    public static GameSessionManager createGameSessionManager(Collection<PlayerData> players) {
        DummyGameEngine gameEngine = new DummyGameEngine();
        return new GameSessionManager(gameEngine, players);
    }
}
