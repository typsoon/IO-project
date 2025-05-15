package frontend.concreteviews.serverlookupview;

import com.badlogic.gdx.scenes.scene2d.Event;

import network.ConnectionData;

public class ServerDataTypedEvent extends Event {
    private final ConnectionData serverData;

    public ServerDataTypedEvent(ConnectionData serverData) {
        this.serverData = serverData;
    }

    public ConnectionData getServerData() {
        return serverData;
    }
}
