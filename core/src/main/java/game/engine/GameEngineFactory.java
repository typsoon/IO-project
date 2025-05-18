package game.engine;

import java.util.Collection;

public class GameEngineFactory {
    public static GameEngine createGameEngine(Collection<EnginePlayerData> players) {
        return new DummyGameEngine(players);
    }
}
