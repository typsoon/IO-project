package network.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;

public interface BytesAccumulator {
    public static enum WhatWasRead {
        TOKEN, MESSAGE
    }

    public static record ReadData(WhatWasRead whatWasRead, ByteBuffer byteBuf) {
    };

    public Optional<ReadData> accumulateBytes(ReadableByteChannel byteIn) throws IOException;
}
