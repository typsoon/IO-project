package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
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

    @Override
    public void sendMessage(Message.EncryptedMessage message) {
        try {
            message.encodeAndWrite(out);

        } catch (IOException e) {
            logger.log(Level.OFF, String.format("An error occured: %s", e));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occured: ", e);
        }
    }

    @Override
    public Message receiveMessage() throws IOException {
        // logger.info("Receiving");
        @SuppressWarnings("unused")
        var msgLen = in.getByte();

        // logger.info("Received msg of len %d".formatted(msgLen));

        return messageDecoder.decodeMessage(in);
    }

}
