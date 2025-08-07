package network.messages;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MessagesConfig {
    public static final Charset msgCharset = StandardCharsets.UTF_8;
    public static final int tokenSize = Integer.BYTES;
}
