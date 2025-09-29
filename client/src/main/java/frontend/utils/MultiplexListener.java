package frontend.utils;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class MultiplexListener implements EventListener {
    private final List<EventListener> listeners = new LinkedList<>();

    public void add(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean handle(Event event) {
        for (EventListener l : listeners) {
            if (l.handle(event)) {
                return true;
            }
        }

        return false;
    }
}
