package network.socketwrappers.concretesocketwrappers;

import java.util.Optional;
import java.util.logging.Logger;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.utils.DataProducer;
import network.messages.utils.DataReceiver;
import network.socketwrappers.sendertypes.LoginStateSender;

import java.io.IOException;

import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;

//TODO: think whether this should implement auto closeable
public class ConcreteAuthenticatingSocket implements LoginStateSender {
  private final DataProducer in;

  private final DataReceiver out;
  private final MessageDecoder messageDecoder;
  private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public ConcreteAuthenticatingSocket(DataProducer in, DataReceiver out) {
    this(in, out, new ConcreteMessageDecoder());
  }

  public ConcreteAuthenticatingSocket(DataProducer in, DataReceiver out, MessageDecoder messageDecoder) {
    this.in = in;
    this.out = out;
    this.messageDecoder = messageDecoder;
  }

  @Override
  public Optional<LogInResponse> sendMessage(LogInQuery message) throws IOException {
    logger.info(String.format("Attempting to log in %s", message));
    message.encodeAndWrite(out);

    // TODO: VERY BIG TODO - think whether doing this read here is ok
    var msgLen = in.getByte();
    var answer = messageDecoder.decodeMessage(in);

    // TODO: this Instanceof fate is probably to be refactored out
    if (answer instanceof LogInResponse logInResponse) {
      return Optional.of(logInResponse);
    } else {
      throw new IllegalStateException(String.format("This is a bad type of response %s", answer));
    }
  }
}
