package network.messages.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import network.messages.MessagesConfig;

public class ByteBufferDataProducer implements DataProducer {
    private final ByteBuffer byteBuffer;
    private final static Charset charset = MessagesConfig.msgCharset;

    public ByteBufferDataProducer(ByteBuffer byteBuffer) throws IOException {
        this.byteBuffer = byteBuffer;
    }

    @Override
    public int getInt() throws IOException {
        // Logger.getGlobal().finest("Pos: %d, Capacity:
        // %d".formatted(byteBuffer.remaining(), byteBuffer.capacity()));
        return byteBuffer.getInt();
    }

    @Override
    public byte getByte() throws IOException {
        // Logger.getGlobal().finest(
        // "Pos: %d, Capacity: %d, Limit: %d".formatted(byteBuffer.position(),
        // byteBuffer.capacity(),
        // byteBuffer.limit()));
        return byteBuffer.get();
    }

    @Override
    public String getString() throws IOException {
        // Logger.getGlobal().finest(
        // "Pos: %d, Capacity: %d, Limit: %d".formatted(byteBuffer.position(),
        // byteBuffer.capacity(),
        // byteBuffer.limit()));

        byte len = byteBuffer.get();

        byte[] strBytes = new byte[len];

        byteBuffer.get(strBytes);

        return new String(strBytes, charset);
    }

    @Override
    public float getFloat() throws IOException {
        return byteBuffer.getFloat();
    }

}
