package game.engine;

import java.util.Collection;

public class DummyGameEngine implements GameEngine {
    @Override
    public void PerformCycle(Collection<Event> events) {
        // Dummy implementation: do nothing
    }

    protected DummyGameEngine(Collection<EnginePlayerData> players) {
        // Dummy implementation: do nothing
    }
}
