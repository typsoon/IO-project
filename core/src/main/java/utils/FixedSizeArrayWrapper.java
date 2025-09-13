package utils;

import java.lang.reflect.Array;
import java.util.Collection;

public class FixedSizeArrayWrapper<T> {
    static final int maxSize = 50;
    private final T[] data;

    @SuppressWarnings("unchecked")
    public FixedSizeArrayWrapper(final Collection<T> data, Class<T> clazz) {
        assert data.size() <= maxSize;

        this.data = data.toArray((T[]) Array.newInstance(clazz, data.size()));
    }

    public final T[] getData() {
        return data;
    }
}
