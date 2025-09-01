package network.messages;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MessagesConfig {
    public static final Charset msgCharset = StandardCharsets.UTF_8;
    public static final int tokenSize = Integer.BYTES;

    public static final int maxStringLen = 1 << 8;
    public static final int maxUdpPacketLength = 1 << 16;
}
