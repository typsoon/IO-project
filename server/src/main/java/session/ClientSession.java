package session;

import java.util.logging.Level;
import java.util.logging.Logger;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.socketwrappers.DuplexSocket;

import java.util.Optional;

public class ClientSession implements Session {
  private final DuplexSocket clientSocket;
  private final Logger logger = Logger.getGlobal();
  // TODO: add credentials validator

  public ClientSession(DuplexSocket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    while (true) {
      try {
        var message = clientSocket.receiveMessage();
        switch (message) {
          case LogInQuery logInQuery -> {
            logger.info(String.format("Log in query received %s", logInQuery));
            boolean was_authenticated = (logInQuery.username().equals("aaa") && logInQuery.password().equals("sd"));

            var response = new LogInResponse(was_authenticated ? Optional.of(2) : Optional.empty());

            logger.info(String.format("Sending response %s", response));
            clientSocket.sendMessage(response);
          }
          default -> {
            throw new IllegalStateException(String.format("Illegal message received: %s", message));
          }
        }

      } catch (Exception e) {
        logger.log(Level.SEVERE, "An occured while receiving message", e);
        return;
      }
    }
  }
}
