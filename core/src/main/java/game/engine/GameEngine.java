package game.engine;

import java.io.Closeable;
import java.util.Collection;

public interface GameEngine extends Closeable {
    void PerformCycle(Collection<Event> events);
}
