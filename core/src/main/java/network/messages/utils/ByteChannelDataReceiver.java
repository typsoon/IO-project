package network.messages.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ByteChannelDataReceiver implements DataReceiver {
    private static final Charset charset = StandardCharsets.UTF_8;
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
}
