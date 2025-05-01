package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.DataProducer;
import network.messages.utils.DataReceiver;
import network.socketwrappers.DuplexSocket;
import java.util.logging.Level;

public class ClientSessionSocket implements DuplexSocket {
  private final DataProducer in;
  private final DataReceiver out;

  private final MessageDecoder messageDecoder;
  private Logger logger = Logger.getGlobal();

  public ClientSessionSocket(DataProducer in, DataReceiver out, MessageDecoder messageDecoder) {
    this.in = in;
    this.out = out;
    this.messageDecoder = messageDecoder;
  }

  public ClientSessionSocket(DataProducer in, DataReceiver out) {
    this(in, out, new ConcreteMessageDecoder());
  }

  @Override
  public Optional<Message> sendMessage(Message message) {
    try {
      message.encodeAndWrite(out);
    } catch (IOException e) {
      logger.log(Level.OFF, String.format("An error occured: %s", e));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occured: ", e);
    }

    return Optional.empty();
  }

  @Override
  public Message receiveMessage() throws IOException {
    return messageDecoder.decodeMessage(in);
  }

}
