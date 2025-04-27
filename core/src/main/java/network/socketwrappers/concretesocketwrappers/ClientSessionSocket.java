package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Logger;

import network.messages.ConcreteMessageDecoder;
import network.messages.Message;
import network.messages.MessageDecoder;
import network.socketwrappers.DuplexSocket;
import java.util.logging.Level;

public class ClientSessionSocket implements DuplexSocket {
  private final Socket socket;
  private final MessageDecoder messageDecoder;
  private Logger logger = Logger.getGlobal();

  public ClientSessionSocket(Socket socket, MessageDecoder messageDecoder) {
    this.messageDecoder = messageDecoder;
    this.socket = socket;
  }

  public ClientSessionSocket(Socket socket) {
    this(socket, new ConcreteMessageDecoder());
  }

  @Override
  public Optional<Message> sendMessage(Message message) {
    try {
      message.encodeAndWrite(socket.getOutputStream());
    } catch (IOException e) {
      logger.log(Level.OFF, "An error occured: ", e);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occured: ", e);
    }

    return Optional.empty();
  }

  @Override
  public Message receiveMessage() throws IOException {
    return messageDecoder.decodeMessage(socket.getInputStream());
  }

}
