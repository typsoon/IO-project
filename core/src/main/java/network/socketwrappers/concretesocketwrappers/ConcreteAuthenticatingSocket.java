package network.socketwrappers.concretesocketwrappers;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.socketwrappers.sendertypes.LoginStateSender;

import java.io.IOException;
import java.net.Socket;

import network.messages.MessageDecoder;
import network.messages.ConcreteMessageDecoder;

//TODO: think whether this should implement auto closeable
public class ConcreteAuthenticatingSocket implements LoginStateSender {
  // TODO: think whether this is ok
  private final Socket socket;
  private final MessageDecoder messageDecoder;
  private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public ConcreteAuthenticatingSocket(Socket socket) {
    this(socket, new ConcreteMessageDecoder());
  }

  public ConcreteAuthenticatingSocket(Socket socket, MessageDecoder messageDecoder) {
    this.socket = socket;
    this.messageDecoder = messageDecoder;
  }

  @Override
  public Optional<LogInResponse> sendMessage(LogInQuery message) {
    logger.info(String.format("Attempting to log in %s", message));
    try {
      message.encodeAndWrite(socket.getOutputStream());

      var in = socket.getInputStream();

      var answer = messageDecoder.decodeMessage(in);

      // TODO: this Instanceof fate is probably to be refactored out
      if (answer instanceof LogInResponse logInResponse) {
        return Optional.of(logInResponse);
      } else {
        throw new IllegalStateException(String.format("This is a bad type of response {0}", answer));
      }

    } catch (IOException e) {
      logger.log(Level.OFF, "An error occured: ", e);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occured: ", e);
    }
    return Optional.empty();
  }
}
