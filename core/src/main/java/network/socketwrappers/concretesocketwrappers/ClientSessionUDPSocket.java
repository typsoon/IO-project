package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import network.messages.Message;
import network.messages.Message.UDPMessage;
import network.messages.MessagesConfig;
import network.messages.decoding.ConcreteMessageDecoder;
import network.messages.decoding.MessageDecoder;
import network.messages.utils.ByteBufferDataConsumer;
import network.messages.utils.ByteBufferDataProducer;
import network.messages.utils.DataConsumer;
import network.socketwrappers.SocketTypes.DuplexSocket;
import network.utils.AccumulatorAdapters;
import network.utils.BytesAccumulator;
import network.utils.TokenView;
import network.utils.impl.OnlyMessagesBytesAccumulator;

public class ClientSessionUDPSocket implements DuplexSocket<UDPMessage> {
    private final DatagramSocket datagramSocket;
    private final ByteBuffer receivingByteBuf = ByteBuffer.wrap(new byte[MessagesConfig.maxUdpPacketLength]).flip();

    private final DatagramPacket receivedPacket = new DatagramPacket(receivingByteBuf.array(),
            receivingByteBuf.capacity());
    private final BytesAccumulator bytesAccumulator = new OnlyMessagesBytesAccumulator();

    private final ByteBuffer sendingByteBuf = ByteBuffer.wrap(new byte[MessagesConfig.maxUdpPacketLength]);
    private final DatagramPacket sentPacket = new DatagramPacket(sendingByteBuf.array(), 0);

    private final MessageDecoder messageDecoder;

    private final TokenView tokenHolder;

    public ClientSessionUDPSocket(DatagramSocket datagramSocket, TokenView tokenView, MessageDecoder messageDecoder) {
        this.datagramSocket = datagramSocket;
        this.messageDecoder = messageDecoder;
        this.tokenHolder = tokenView;
    }

    public ClientSessionUDPSocket(DatagramSocket datagramSocket, TokenView tokenView) {
        this(datagramSocket, tokenView, new ConcreteMessageDecoder());
    }

    private final DataConsumer consumer = new ByteBufferDataConsumer(sendingByteBuf);

    @Override
    public final void sendMessage(UDPMessage message) throws IOException {
        // TODO: maybe send multiple messages in one Datagram
        sendingByteBuf.clear();

        consumer.putInt(tokenHolder.getToken());

        // Logger.getGlobal().finer("Put token bytes in
        // %d".formatted(tokenHolder.getToken()));

        message.encodeAndWrite(consumer);

        // NOTE: debug
        // int oldPos = sendingByteBuf.position();
        // int oldLimit = sendingByteBuf.limit();
        // sendingByteBuf.flip();
        // DebugUtils.printBuffer(sendingByteBuf);
        // sendingByteBuf.position(oldPos);
        // sendingByteBuf.limit(oldLimit);

        sentPacket.setLength(sendingByteBuf.position());

        datagramSocket.send(sentPacket);
    }

    private final BytesAccumulator.Readable receivingByteBufAdapter = AccumulatorAdapters
            .getByteBufferAdapter(receivingByteBuf);

    @Override
    public final Message receiveMessage() throws IOException {
        do {
            if (receivingByteBuf.hasRemaining()) {
                var readRes = bytesAccumulator.accumulateBytes(receivingByteBufAdapter);

                if (readRes.isPresent()) {
                    // Logger.getGlobal().info("%s %d bytes
                    // remaining".formatted(readRes.get().whatWasRead(),
                    // readRes.get().byteBuf().remaining()));

                    var dataProducer = new ByteBufferDataProducer(readRes.get().byteBuf());
                    return messageDecoder.decodeMessage(dataProducer);
                }
            }

            receivingByteBuf.clear();

            // Logger.getGlobal().info("RECEIVING OVER UDP");
            datagramSocket.receive(receivedPacket);
            // Logger.getGlobal().info(
            // "RECEIVED %d BYTES OVER UDP".formatted(receivedPacket.getLength()));
            receivingByteBuf.limit(receivedPacket.getLength());
        } while (true);
    }
}
