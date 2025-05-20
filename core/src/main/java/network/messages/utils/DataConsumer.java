package network.messages.utils;

import java.io.IOException;

public interface DataConsumer {
    void putInt(int val) throws IOException;

    void putByte(byte val) throws IOException;

    void putString(String str) throws IOException;
}
