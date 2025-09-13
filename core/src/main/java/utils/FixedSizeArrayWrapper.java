package utils;

import java.util.Collection;

public class FixedSizeArrayWrapper<T> {
    static final int maxSize = 50;
    private final T[] data;

    @SuppressWarnings("unchecked")
    public FixedSizeArrayWrapper(final Collection<T> data) {
        assert data.size() <= maxSize;

        this.data = (T[]) data.toArray();
    }

    public final T[] getData() {
        return data;
    };
}
