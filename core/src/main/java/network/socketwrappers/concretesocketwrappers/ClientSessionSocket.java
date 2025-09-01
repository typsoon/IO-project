package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.DataProducer;
import network.socketwrappers.SocketTypes.DuplexSocket;
import network.utils.TokenView;
import network.messages.utils.DataConsumer;

import java.util.logging.Level;

public class ClientSessionSocket<T extends Message> implements DuplexSocket<T> {
    private final DataProducer in;
    private final DataConsumer out;
    private final TokenView tokenHolder;

    private final MessageDecoder messageDecoder;
    private Logger logger = Logger.getGlobal();

    public ClientSessionSocket(DataProducer in, DataConsumer out, TokenView tokenHolder,
            MessageDecoder messageDecoder) {
        this.in = in;
        this.out = out;
        this.messageDecoder = messageDecoder;
        this.tokenHolder = tokenHolder;
    }

    public ClientSessionSocket(DataProducer in, DataConsumer out, TokenView tokenHolder) {
        this(in, out, tokenHolder, new ConcreteMessageDecoder());
    }

    @Override
    public void sendMessage(Message message) {
        try {
            out.putInt(tokenHolder.getToken());

            // TODO: remove this
            logger.finer("Put token bytes in %d".formatted(tokenHolder.getToken()));

            message.encodeAndWrite(out);

            logger.finer("Written %s".formatted(message.getSendable()));
        } catch (IOException e) {
            logger.log(Level.OFF, String.format("An error occured: %s", e));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occured: ", e);
        }

        // return Optional.empty();
    }

    @Override
    public Message receiveMessage() throws IOException {
        return messageDecoder.decodeMessage(in);
    }

}
