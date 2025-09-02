package network.messages.utils;

import java.io.IOException;

import game.utility.Point2F;
import game.utility.Vector2F;

public interface DataConsumer {
    void putInt(int val) throws IOException;

    void putByte(byte val) throws IOException;

    void putString(String str) throws IOException;

    void putFloat(float val) throws IOException;

    default void putBoolean(boolean val) throws IOException {
        putByte((byte) (val ? 1 : 0));
    }

    default void putPoint2F(Point2F point) throws IOException {
        putFloat(point.x());
        putFloat(point.y());
    };

    default void putVector2F(Vector2F vector) throws IOException {
        putFloat(vector.x());
        putFloat(vector.y());
    };

    default <T extends Enum<T>> void putEnum(T enumVar) throws IOException {
        putInt(enumVar.ordinal());
    }
}
