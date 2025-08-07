package network.server.nio.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;
import java.util.logging.Logger;

import network.server.nio.BytesAccumulator;
import network.messages.MessagesConfig;

public class OrdinaryBytesAccumulator implements BytesAccumulator {
    private static enum State {
        TOKEN_NOT_READ, TOKEN_READ
    }

    private Optional<ByteBuffer> currBuffer = Optional.empty();
    private final Logger logger = Logger.getGlobal();
    private State state = State.TOKEN_NOT_READ;

    private void createBuffer(ReadableByteChannel in) throws IOException {
        if (State.TOKEN_NOT_READ.equals(state)) {
            currBuffer = Optional.of(ByteBuffer.allocate(MessagesConfig.tokenSize));
            logger.finest("Allocated buffer for token: buffer capacity: %s".formatted(currBuffer.get().capacity()));
            return;
        }

        ByteBuffer msgSizeBuf = ByteBuffer.allocate(Byte.BYTES);
        var readRes = in.read(msgSizeBuf);

        if (readRes == -1) {
            // TODO: Connection has ended
            throw new IllegalStateException("Connection has ended");
        }
        msgSizeBuf.flip();
        var msgSize = msgSizeBuf.get();
        logger.finest("Message size: %s".formatted(msgSize));

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
            switch (state) {
                case TOKEN_NOT_READ -> {
                    state = State.TOKEN_READ;
                    return Optional.of(new ReadData(WhatWasRead.TOKEN, actBufferUnwrapped));
                }
                case TOKEN_READ -> {
                    state = State.TOKEN_NOT_READ;
                    return Optional.of(new ReadData(WhatWasRead.MESSAGE, actBufferUnwrapped));
                }
                default -> {
                    throw new IllegalStateException();
                }
            }
        } finally {
            currBuffer = Optional.empty();
        }
    }
}
