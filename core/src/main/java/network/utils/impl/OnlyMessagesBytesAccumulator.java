package network.utils.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.logging.Logger;

import network.utils.BytesAccumulator;

public class OnlyMessagesBytesAccumulator implements BytesAccumulator {
    private Optional<ByteBuffer> currBuffer = Optional.empty();
    private final Logger logger = Logger.getGlobal();

    private boolean createBuffer(Readable in) throws IOException {
        ByteBuffer msgSizeBuf = ByteBuffer.allocate(Byte.BYTES);
        var readRes = in.read(msgSizeBuf);

        if (readRes == -1) {
            // TODO: Connection has ended
            throw new IllegalStateException("Connection has ended");
        }
        if (readRes == 0) {
            return false;
        }

        msgSizeBuf.flip();
        var msgSize = msgSizeBuf.get();
        logger.finest("Message size: %s".formatted(msgSize));

        currBuffer = Optional.of(ByteBuffer.allocate(msgSize));
        return true;
    }

    public Optional<ReadData> accumulateBytes(Readable byteIn)
            throws IOException {
        if (currBuffer.isEmpty()) {
            if (!createBuffer(byteIn)) {
                return Optional.empty();
            }
        }

        var actBufferUnwrapped = currBuffer.get();
        while (actBufferUnwrapped.hasRemaining()) {
            var readRes = byteIn.read(actBufferUnwrapped);

            if (readRes == 0) {
                Logger.getGlobal().finest("No bytes read, waiting for more data");
                return Optional.empty();
            } else if (readRes == -1) {
                // TODO: Close session
                throw new IllegalStateException("Connection closed: %d returned from read".formatted(readRes));
            }
        }
        actBufferUnwrapped.flip();

        Logger.getGlobal().finest("ByteBufInfo pos %d cap %d lim %d".formatted(actBufferUnwrapped.position(),
                actBufferUnwrapped.capacity(), actBufferUnwrapped.limit()));

        try {
            return Optional.of(new ReadData(WhatWasRead.MESSAGE, actBufferUnwrapped));
        } finally {
            currBuffer = Optional.empty();
        }
    }
}
