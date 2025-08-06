package frontend.concreteviews.loginview;

import com.badlogic.gdx.scenes.scene2d.Event;

import network.utils.Credentials;

public class CredentialsTypedEvent extends Event {
    private final Credentials credentials;

    public CredentialsTypedEvent(final Credentials credentials) {
        this.credentials = credentials;
    }

    Credentials getCredentials() {
        return credentials;
    }
}
