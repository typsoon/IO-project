package network.messages.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import game.utility.Point2F;
import game.utility.Vector2F;

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

    static final Point2F point2F;
    static final Vector2F vector2F;
    static final byte[] encodedPoint2F;
    static final byte[] encodedVector2F;
    static {
        point2F = new Point2F(1.2f, 3.4f);
        vector2F = new Vector2F(5.6f, 7.8f);

        encodedPoint2F = new byte[Point2F.BYTES];
        ByteBuffer.wrap(encodedPoint2F).putFloat(point2F.x()).putFloat(point2F.y());

        encodedVector2F = new byte[Vector2F.BYTES];
        ByteBuffer.wrap(encodedVector2F).putFloat(vector2F.x()).putFloat(vector2F.y());
    }

    static final int integer = -4567;
    static final byte[] encodedInteger;

    static {
        encodedInteger = new byte[Integer.BYTES];
        ByteBuffer.wrap(encodedInteger).putInt(integer);
    }
}
