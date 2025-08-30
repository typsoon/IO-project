package network.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DebugUtils {

    public static void printBuffer(ByteBuffer byteBuffer) {
        List<Byte> bytes = new ArrayList<>();

        byteBuffer.mark();

        while (byteBuffer.hasRemaining()) {
            bytes.add(byteBuffer.get());

        }
        byteBuffer.reset();

        Logger.getGlobal().info("bytes: %s".formatted(bytes.toString()));
    }

}
