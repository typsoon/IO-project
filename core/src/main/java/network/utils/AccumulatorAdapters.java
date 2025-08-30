package network.utils;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import network.utils.BytesAccumulator.Readable;

public class AccumulatorAdapters {
    public static Readable getRedableByteChannelAdapter(final ReadableByteChannel readableByteChannel) {
        return readableByteChannel::read;
    }

    public static Readable getByteBufferAdapter(final ByteBuffer wrappedByteBuf) {
        return byteBuffer -> {
            final int res = Math.min(byteBuffer.remaining(), wrappedByteBuf.remaining());

            final int oldLimit = wrappedByteBuf.limit();
            wrappedByteBuf.limit(wrappedByteBuf.position() + res);
            byteBuffer.put(wrappedByteBuf);
            wrappedByteBuf.limit(oldLimit);

            return res;
        };
    }
}
