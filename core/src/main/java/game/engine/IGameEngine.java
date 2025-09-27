package game.engine;

import utils.IDisposable;

import java.util.Collection;

public interface IGameEngine extends IDisposable {
    void performCycle(Collection<Event> events);
}
