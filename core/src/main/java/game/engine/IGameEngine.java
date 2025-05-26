package game.engine;

import java.io.Closeable;
import java.util.Collection;

public interface IGameEngine extends Closeable {
    void PerformCycle(Collection<Event> events);
}
