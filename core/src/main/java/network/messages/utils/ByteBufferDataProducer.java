package network.messages.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ByteBufferDataProducer implements DataProducer {
    private final ByteBuffer byteBuffer;
    private final static Charset charset = StandardCharsets.UTF_8;

    public ByteBufferDataProducer(ByteBuffer byteBuffer) throws IOException {
        this.byteBuffer = byteBuffer;
    }

    @Override
    public int getInt() throws IOException {
        Logger.getGlobal().info("Pos: %d, Capacity: %d".formatted(byteBuffer.remaining(), byteBuffer.capacity()));
        return byteBuffer.getInt();
    }

    @Override
    public byte getByte() throws IOException {
        Logger.getGlobal().info(
                "Pos: %d, Capacity: %d, Limit: %d".formatted(byteBuffer.position(), byteBuffer.capacity(), byteBuffer.limit()));
        return byteBuffer.get();
    }

    @Override
    public String getString() throws IOException {
        byte len = byteBuffer.get();
        byte[] strBytes = new byte[len];
        byteBuffer.get(strBytes);
        return new String(strBytes, charset);
    }

}
