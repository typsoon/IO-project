package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.badlogic.gdx.graphics.g3d.environment.AmbientCubemap;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.utils.DataConsumer;
import network.messages.utils.DataProducer;
import network.socketwrappers.SenderTypes.LoginStateSender;

//TODO: think whether this should implement auto closeable
public class ConcreteAuthenticatingSocket implements LoginStateSender {
    private final DataProducer in;

    private final DataConsumer out;
    private final MessageDecoder messageDecoder;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public ConcreteAuthenticatingSocket(DataProducer in, DataConsumer out) {
        this(in, out, new ConcreteMessageDecoder());
    }

    public ConcreteAuthenticatingSocket(DataProducer in, DataConsumer out, MessageDecoder messageDecoder) {
        this.in = in;
        this.out = out;
        this.messageDecoder = messageDecoder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Message> Optional<T> sendMessage(LogInQuery message) throws IOException {
        logger.info(String.format("Attempting to log in %s", message));
        message.encodeAndWrite(out);

        // TODO: VERY BIG TODO - think whether doing this read here is ok
        var msgLen = in.getByte();
        var answer = messageDecoder.decodeMessage(in);

        try {
            return Optional.of((T) answer);
        } catch (Exception e) {
            logger.warning(String.format("An error occured while receiving the response: %s", e));
            throw new IllegalStateException(String.format("This is a bad type of response %s", e));
        }
    }
}
