package session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.utils.ByteBufferDataProducer;
import network.messages.utils.ByteChannelDataReceiver;
import network.socketwrappers.SocketTypes.DuplexSocket;

import static session.HandlingResult.*;

public class ClientSession implements Session {
    private final DuplexSocket clientSocket;
    private final MessageDecoder messageDecoder;
    private final Logger logger = Logger.getGlobal();

    private ByteBuffer actMessageBuffer = null;

    public ClientSession(DuplexSocket clientSocket, MessageDecoder msgDecoder) {
        this.clientSocket = clientSocket;
        this.messageDecoder = msgDecoder;
    }

    // TODO: add credentials validator
    // TODO: remove magic number from here
    private final Collection<Message> pendingResponses = new LinkedBlockingQueue<>(512);

    public ClientSession(DuplexSocket clientSocket) {
        this(clientSocket, new ConcreteMessageDecoder());
    }

    private final boolean startedReceiving() {
        return actMessageBuffer != null;
    }

    private final void allocateSpaceForMessage(int capacity) {
        actMessageBuffer = ByteBuffer.allocate(capacity);
    }

    private HandlingResult prepareAMessageForSending() throws IOException {
        actMessageBuffer.flip();
        var producer = new ByteBufferDataProducer(actMessageBuffer);
        var message = messageDecoder.decodeMessage(producer);
        actMessageBuffer = null;
        return handleMessage(message);
    }

    @Override
    public HandlingResult handleMessage(Message message) {
        switch (message) {
            case LogInQuery logInQuery -> {

                logger.info(String.format("Log in query received %s", logInQuery));
                boolean was_authenticated = (logInQuery.username().equals("aaa") && logInQuery.password().equals("sd"));

                var response = new LogInResponse(was_authenticated ? Optional.of(2) : Optional.empty());

                pendingResponses.add(response);

                return SHOULD_RESPOND;
            }
            default -> {
                throw new IllegalStateException(String.format("Unexpected message received %s", message));
            }
        }
        // TODO Auto-generated method stub
    }

    @Override
    public void sendResponses(WritableByteChannel channel) throws IOException {
        Collection<Message> succesfullySent = new ArrayList<>();

        try {
            for (Message response : pendingResponses) {
                logger.info(String.format("Sending response %s", response));
                response.encodeAndWrite(new ByteChannelDataReceiver(channel));
                succesfullySent.add(response);
            }
        } catch (IOException e) {
            pendingResponses.removeAll(succesfullySent);
            logger.severe(
                    String.format("An error occured while sending. Error: %s, Unsent messages: %s", pendingResponses));
            throw e;
        }
        pendingResponses.clear();
    }

    @Override
    public HandlingResult handleIncomingBytes(ReadableByteChannel byteIn) throws IOException {
        if (!startedReceiving()) {
            ByteBuffer msgSizeBuf = ByteBuffer.allocate(Byte.BYTES);
            var readRes = byteIn.read(msgSizeBuf);

            if (readRes == -1) {
                return CONNECTION_ENDED;
            }

            msgSizeBuf.flip();
            allocateSpaceForMessage(msgSizeBuf.get());
        }

        HandlingResult shouldSend = DONT_RESPOND;

        while (true) {
            var readRes = byteIn.read(actMessageBuffer);

            if (actMessageBuffer.position() == actMessageBuffer.capacity()) {
                shouldSend = prepareAMessageForSending();
                break;
            }

            if (readRes == 0) {
                break;
            } else if (readRes == -1) {
                // TODO: Close session
                shouldSend = CONNECTION_ENDED;
                break;
            }
        }

        return shouldSend;
    }
}
