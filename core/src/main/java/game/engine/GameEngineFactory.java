package game.engine;

import java.util.Collection;

public class GameEngineFactory {
    public static GameEngine createGameEngine(Collection<EnginePlayerData> players) {
        // Here will be created modules of GameEngine
        return new DummyGameEngine(players);
    }
}
