package network.server.nio.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;
import java.util.logging.Logger;

import network.server.nio.BytesAccumulator;

public class SSLSocketBytesAccumulator implements BytesAccumulator {
    private Optional<ByteBuffer> currBuffer = Optional.empty();
    private final Logger logger = Logger.getGlobal();

    private void createBuffer(ReadableByteChannel in) throws IOException {
        ByteBuffer msgSizeBuf = ByteBuffer.allocate(Byte.BYTES);
        var readRes = in.read(msgSizeBuf);

        if (readRes == -1) {
            // TODO: Connection has ended
            throw new IllegalStateException("Connection has ended");
        }
        msgSizeBuf.flip();
        var msgSize = msgSizeBuf.get();
        logger.info("Message size: %s".formatted(msgSize));

        currBuffer = Optional.of(ByteBuffer.allocate(msgSize));
    }

    public Optional<ReadData> accumulateBytes(ReadableByteChannel byteIn)
            throws IOException {
        if (currBuffer.isEmpty()) {
            createBuffer(byteIn);
        }

        var actBufferUnwrapped = currBuffer.get();
        while (actBufferUnwrapped.position() != actBufferUnwrapped.capacity()) {
            var readRes = byteIn.read(actBufferUnwrapped);

            if (readRes == 0) {
                Logger.getGlobal().info("No bytes read, waiting for more data");
                return Optional.empty();
            } else if (readRes == -1) {
                // TODO: Close session
                throw new IllegalStateException("Connection closed: %d returned from read".formatted(readRes));
            }
        }
        actBufferUnwrapped.flip();

        Logger.getGlobal().info("ByteBufInfo pos %d cap %d lim %d".formatted(actBufferUnwrapped.position(),
                actBufferUnwrapped.capacity(), actBufferUnwrapped.limit()));

        try {
            return Optional.of(new ReadData(WhatWasRead.MESSAGE, actBufferUnwrapped));
        } finally {
            currBuffer = Optional.empty();
        }
    }
}
