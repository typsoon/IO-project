package network.messages.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import network.messages.MessagesConfig;

public class ByteChannelDataReceiver implements DataConsumer {
    private static final Charset charset = MessagesConfig.msgCharset;
    private final WritableByteChannel out;

    public ByteChannelDataReceiver(WritableByteChannel out) {
        this.out = out;
    }

    @Override
    public void putInt(int val) throws IOException {
        var buf = ByteBuffer.allocate(Integer.BYTES)
                .putInt(val)
                .flip();
        out.write(buf);
    }

    @Override
    public void putByte(byte val) throws IOException {
        var buf = ByteBuffer.allocate(Byte.BYTES)
                .put(val)
                .flip();
        out.write(buf);
    }

    @Override
    public void putString(String str) throws IOException {
        byte[] strBytes = str.getBytes(charset);

        ByteBuffer byteBuf = ByteBuffer.allocate(Byte.BYTES + strBytes.length)
                .put((byte) strBytes.length)
                .put(strBytes)
                .flip();

        out.write(byteBuf);
    }

    @Override
    public void putFloat(float val) throws IOException {
        var buf = ByteBuffer.allocate(Float.BYTES)
                .putFloat(val)
                .flip();
        out.write(buf);
    }
}
