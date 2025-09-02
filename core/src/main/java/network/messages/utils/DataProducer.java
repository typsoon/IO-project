package network.messages.utils;

import java.io.IOException;

import game.utility.Point2F;
import game.utility.Vector2F;

public interface DataProducer {
    int getInt() throws IOException;

    byte getByte() throws IOException;

    String getString() throws IOException;

    float getFloat() throws IOException;

    default boolean getBoolean() throws IOException {
        return getByte() != 0;
    }

    default Point2F getPoint2F() throws IOException {
        return new Point2F(getFloat(), getFloat());
    }

    default Vector2F getVector2F() throws IOException {
        return new Vector2F(getFloat(), getFloat());
    }

    default <T extends Enum<T>> T getEnum(T[] values) throws IOException {
        return values[getInt()];
    }
}
