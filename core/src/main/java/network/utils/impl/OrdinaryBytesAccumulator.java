package network.utils.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.logging.Logger;

import network.messages.MessagesConfig;
import network.utils.BytesAccumulator;

public class OrdinaryBytesAccumulator implements BytesAccumulator {
    private static enum State {
        TOKEN_NOT_READ, TOKEN_READ
    }

    private Optional<ByteBuffer> currBuffer = Optional.empty();
    private final Logger logger = Logger.getGlobal();
    private State state = State.TOKEN_NOT_READ;

    // true if a buffer was created
    private boolean createBuffer(Readable in) throws IOException {
        if (State.TOKEN_NOT_READ.equals(state)) {
            currBuffer = Optional.of(ByteBuffer.allocate(MessagesConfig.tokenSize));
            logger.finest("Allocated buffer for token: buffer capacity: %s".formatted(currBuffer.get().capacity()));
            return true;
        }

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
        logger.finest(
                "limit %d position %d remaining %d capacity %d".formatted(msgSizeBuf.limit(), msgSizeBuf.position(),
                        msgSizeBuf.remaining(), msgSizeBuf.capacity()));
        var msgSize = msgSizeBuf.get();
        logger.finest("Message size: %s".formatted(msgSize));

        currBuffer = Optional.of(ByteBuffer.allocate(msgSize));

        return true;
    }

    @Override
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

        // TODO: remove this try catch block - it is not good for performance
        try {
            return switch (state) {
                case TOKEN_NOT_READ -> {
                    state = State.TOKEN_READ;
                    yield Optional.of(new ReadData(WhatWasRead.TOKEN, actBufferUnwrapped));
                }
                case TOKEN_READ -> {
                    state = State.TOKEN_NOT_READ;
                    yield Optional.of(new ReadData(WhatWasRead.MESSAGE, actBufferUnwrapped));
                }
            };
        } finally {
            currBuffer = Optional.empty();
        }
    }
}
