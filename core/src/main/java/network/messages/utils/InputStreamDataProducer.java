package network.messages.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import network.messages.MessagesConfig;

public class InputStreamDataProducer implements DataProducer {
    private final NoCloseDataInputStream in;
    private final Charset charset = MessagesConfig.msgCharset;

    public InputStreamDataProducer(InputStream inputStream) {
        this.in = new NoCloseDataInputStream(inputStream);
    }

    @Override
    public int getInt() throws IOException {
        return in.readInt();
    }

    @Override
    public byte getByte() throws IOException {
        return in.readByte();
    }

    @Override
    public String getString() throws IOException {
        var stringLen = in.readByte();
        var strBytes = new byte[stringLen];
        in.readFully(strBytes);
        return new String(strBytes, charset);
    }

    @Override
    public float getFloat() throws IOException {
        return in.readFloat();
    }
}

/**
 * This class exists because I want to read data e.g ints from InputStream
 * without getting
 * linter errors (I know that I can disable them but I chose this way) and
 * closing the underlying InputStream
 */
class NoCloseDataInputStream extends DataInputStream {
    protected NoCloseDataInputStream(InputStream in) {
        super(in);
    }

    @Override
    public void close() throws IOException {
    }
}
