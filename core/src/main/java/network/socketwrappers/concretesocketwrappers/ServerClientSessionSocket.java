package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.DataProducer;
import network.socketwrappers.SocketTypes.DuplexSocket;
import network.messages.utils.DataConsumer;

import java.util.logging.Level;

public class ServerClientSessionSocket<T extends Message> implements DuplexSocket<T> {
    private final DataProducer in;
    private final DataConsumer out;

    private final MessageDecoder messageDecoder;
    private Logger logger = Logger.getGlobal();

    public ServerClientSessionSocket(DataProducer in, DataConsumer out,
            MessageDecoder messageDecoder) {
        this.in = in;
        this.out = out;
        this.messageDecoder = messageDecoder;
    }

    public ServerClientSessionSocket(DataProducer in, DataConsumer out) {
        this(in, out, new ConcreteMessageDecoder());
    }

    @Override
    public void sendMessage(Message message) {
        try {
            message.encodeAndWrite(out);
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
