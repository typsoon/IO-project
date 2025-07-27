package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.loginstate.LogInResponse;
import network.messages.utils.DataConsumer;
import network.messages.utils.DataProducer;
import network.socketwrappers.SocketTypes.DuplexSocket;

public class ClientSessionSSLSocket implements DuplexSocket<Message.EncryptedMessage> {
    private final DataProducer in;
    private final DataConsumer out;

    private final MessageDecoder messageDecoder;
    private Logger logger = Logger.getGlobal();

    public ClientSessionSSLSocket(DataProducer in, DataConsumer out, MessageDecoder messageDecoder) {
        this.in = in;
        this.out = out;
        this.messageDecoder = messageDecoder;
    }

    public ClientSessionSSLSocket(DataProducer in, DataConsumer out) {
        this(in, out, new ConcreteMessageDecoder());
    }

    // TODO: think about making this public <T> Optional<T> sendMessage
    @Override
    public Optional<Message> sendMessage(Message.EncryptedMessage message) {
        try {
            message.encodeAndWrite(out);

            var msgLen = in.getByte();
            var answer = messageDecoder.decodeMessage(in);

            try {
                return Optional.of((LogInResponse) answer);
            } catch (Exception e) {
                logger.warning(String.format("An error occured while receiving the response: %s", e));
                throw new IllegalStateException(String.format("This is a bad type of response %s", e));
            }

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
