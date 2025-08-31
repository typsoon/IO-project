package network.utils.impl;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.junit.jupiter.api.Assertions.*;

import network.utils.AccumulatorAdapters;

class AccumulatorAdaptersTest {

    @Test
    void ByteChannelAdapterReadsData() throws IOException {
        byte[] input = "hello".getBytes();
        ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(input));
        var readable = AccumulatorAdapters.getRedableByteChannelAdapter(channel);

        ByteBuffer buf = ByteBuffer.allocate(10);
        int read = readable.read(buf);

        assertEquals(input.length, read, "Should read all bytes");
        buf.flip();
        byte[] out = new byte[buf.remaining()];
        buf.get(out);

        assertArrayEquals(input, out, "Output should match input");
    }

    @Test
    void byteChannelAdapterEndOfStream() throws IOException {
        ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(new byte[0]));
        var readable = AccumulatorAdapters.getRedableByteChannelAdapter(channel);

        ByteBuffer buf = ByteBuffer.allocate(5);
        int read = readable.read(buf);

        assertEquals(-1, read, "Should return -1 at end of stream");
    }

    @Test
    void byteBufferAdapterReadsIntoTargetBuffer() throws IOException {
        byte[] data = { 1, 2, 3, 4 };
        ByteBuffer wrapped = ByteBuffer.wrap(data);

        var readable = AccumulatorAdapters.getByteBufferAdapter(wrapped);

        ByteBuffer target = ByteBuffer.allocate(4);
        int read = readable.read(target);

        assertEquals(4, read, "Should copy all bytes");
        target.flip();
        byte[] out = new byte[target.remaining()];
        target.get(out);

        assertArrayEquals(data, out, "Copied bytes should match original");
        assertEquals(0, wrapped.remaining(), "Wrapped buffer should be consumed");
    }

    @Test
    void byteBufferAdapterPartialRead() throws IOException {
        byte[] data = { 10, 20, 30, 40 };
        ByteBuffer wrapped = ByteBuffer.wrap(data);

        var readable = AccumulatorAdapters.getByteBufferAdapter(wrapped);

        ByteBuffer target = ByteBuffer.allocate(2);
        int read = readable.read(target);

        assertEquals(2, read, "Should copy only 2 bytes");
        target.flip();
        assertEquals(10, target.get());
        assertEquals(20, target.get());
        assertEquals(2, wrapped.remaining(), "Two bytes should remain in wrapped buffer");
    }
}
