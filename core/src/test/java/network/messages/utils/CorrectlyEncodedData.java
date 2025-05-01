package network.messages.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

final class CorrectlyEncodedData {
  private static final Charset charset = StandardCharsets.UTF_8;
  static final String string = """
      Life: a fleeting dance of entropy, pretending to be order, briefly grasping meaning before fading into silence.
      """;
  static final byte[] encodedString;

  static {
    var encodedStrBytes = string.getBytes(charset);

    ByteBuffer tempByteBuf = ByteBuffer.allocate(Byte.BYTES + encodedStrBytes.length);
    tempByteBuf.put((byte) encodedStrBytes.length)
        .put(encodedStrBytes);

    encodedString = tempByteBuf.array();
  }

  static final int integer = -4567;
  static final byte[] encodedInteger;

  static {
    encodedInteger = new byte[Integer.BYTES];
    ByteBuffer.wrap(encodedInteger).putInt(integer);
  }
}
