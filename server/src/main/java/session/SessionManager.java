package session;

import java.util.logging.Level;
import java.util.logging.Logger;

import network.ServerSocketInjector;

public class SessionManager {
  private final AbstractSessionFactory sessionFactory;
  private final int port;

  public SessionManager(AbstractSessionFactory abstractSessionFactory, int port) {
    this.sessionFactory = abstractSessionFactory;
    this.port = port;
  }

  public void start() {
    // TODO: remove dependency Logging
    var injector = new ServerSocketInjector();
    try (var serverSocket = injector.getServerSocketWrapper(port)) {
      Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
          // .info(String.format("Server started at port: %d, InetAdress: {s}", port));
          .info(String.format("Server started at port: %d", port));

      while (true) {
        var clientSocket = serverSocket.acceptClient();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(String.format("Client connected: %s", clientSocket));

        var session = sessionFactory.getSession(clientSocket);
        new Thread(session).start();
      }

    } catch (Exception e) {
      Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Error occured {0}", e.getStackTrace());
    }

  };
}
