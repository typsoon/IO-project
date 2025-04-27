package session;

public class SessionManagerInjector {
  public SessionManager getSessionManager(int port) {
    var sessionfactory = new ConcreteSessionFactory();
    return new SessionManager(sessionfactory, port);
  }
}
