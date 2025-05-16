package game.engine;

import java.util.Collection;

public interface GameEngine {
    void PerformCycle(Collection<Event> events);
}
