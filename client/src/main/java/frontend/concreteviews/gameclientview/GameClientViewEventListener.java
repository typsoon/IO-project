package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import network.client.ClientSideSocketWrapper;
import viewmodel.AbstractViewManager;

public class GameClientViewEventListener implements EventListener {
    private final AbstractViewManager viewManager;
    private final ClientSideSocketWrapper clientSideSocketWrapper;

    public GameClientViewEventListener(AbstractViewManager viewManager,
            ClientSideSocketWrapper clientSideSocketWrapper) {
        this.viewManager = viewManager;
        this.clientSideSocketWrapper = clientSideSocketWrapper;
    }

    @Override
    public boolean handle(Event event) {
        // TODO: write this
        return false;
    }
}
