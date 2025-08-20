package game.engine;

import java.io.Closeable;
import java.util.Collection;

public interface IGameEngine extends Closeable {
    void performCycle(Collection<Event> events);
}
