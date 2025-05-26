package network.server.nio.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;
import java.util.logging.Logger;

import network.server.nio.BytesAccumulator;

public class OrdinaryBytesAccumulator implements BytesAccumulator {
    // private final Logger logger = Logger.getGlobal();
    //
    public static enum WhatWasRead {
        TOKEN, MESSAGE
    }

    public static record ReadData(WhatWasRead whatWasRead, ByteBuffer byteBuf) {
    };

    private WhatWasRead whatWeAreExpectingToRead = WhatWasRead.TOKEN;

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

    private Optional<ByteBuffer> accumulateBytess(ReadableByteChannel byteIn) throws IOException {
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
        return Optional.of(actBufferUnwrapped);
    }

    @Override
    public Optional<network.server.nio.BytesAccumulator.ReadData> accumulateBytes(ReadableByteChannel byteIn)
            throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accumulateBytes'");
    }
}
