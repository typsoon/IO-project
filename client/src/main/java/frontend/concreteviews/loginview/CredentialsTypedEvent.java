package frontend.concreteviews.loginview;

import com.badlogic.gdx.scenes.scene2d.Event;

import network.Credentials;

public class CredentialsTypedEvent extends Event {
  private final Credentials credentials;

  public CredentialsTypedEvent(Credentials credentials) {
    this.credentials = credentials;
  }

  Credentials getCredentials() {
    return credentials;
  }
}
