package network;

public interface AuthenticatingSocket {
  AuthRequestResponse tryLogIn(Credentials credentials);
}
