package network.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

public interface BytesAccumulator {
    @FunctionalInterface
    public static interface Readable {
        int read(ByteBuffer byteBuffer) throws IOException;
    }

    public static enum WhatWasRead {
        TOKEN, MESSAGE
    }

    public static record ReadData(WhatWasRead whatWasRead, ByteBuffer byteBuf) {
    };

    public Optional<ReadData> accumulateBytes(Readable byteIn) throws IOException;
}
