package network.messages.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import network.messages.MessagesConfig;

public class ByteBufferDataConsumer implements DataConsumer {
    private static final Charset charset = MessagesConfig.msgCharset;
    private final ByteBuffer wrappedByteBuffer;

    public ByteBufferDataConsumer(ByteBuffer wrappedByteBuffer) {
        this.wrappedByteBuffer = wrappedByteBuffer;
    }

    @Override
    public void putInt(int val) throws IOException {
        wrappedByteBuffer.putInt(val);
    }

    @Override
    public void putByte(byte val) throws IOException {
        wrappedByteBuffer.put(val);
    }

    @Override
    public void putString(String str) throws IOException {
        byte[] strBytes = str.getBytes(charset);

        wrappedByteBuffer
                .put((byte) strBytes.length)
                .put(strBytes);
    }

    @Override
    public void putFloat(float val) throws IOException {
        wrappedByteBuffer.putFloat(val);
    }

}
