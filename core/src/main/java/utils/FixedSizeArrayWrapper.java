package utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FixedSizeArrayWrapper<T> {
    static final int maxSize = 50;
    private final T[] data;

    @SuppressWarnings("unchecked")
    private static final <T> T[] getDataFromCollectionAndClass(final Collection<T> data, Class<T> clazz) {
        return data.toArray((T[]) Array.newInstance(clazz, data.size()));
    }

    public FixedSizeArrayWrapper(final Collection<T> data, Class<T> clazz) {
        assert data.size() <= maxSize;

        this.data = getDataFromCollectionAndClass(data, clazz);
    }

    // @SuppressWarnings("unchecked")
    // public FixedSizeArrayWrapper(final Collection<T> data) {
    // assert data.size() <= maxSize;
    //
    // if (data.isEmpty()) {
    // this.data = (T[]) new Object[0];
    // return;
    // }
    //
    // final var clazz = (Class<T>) data.iterator().next().getClass();
    // this.data = getDataFromCollectionAndClass(data, clazz);
    // }

    public final static <T> FixedSizeArrayWrapper<T> getArrayFromSupplier(int len, Supplier<T> supplier,
            Class<T> clazz) {
        final LinkedList<T> collection = new LinkedList<>();

        for (int i = 0; i < len; i++) {
            collection.add(supplier.get());
        }

        return new FixedSizeArrayWrapper<T>(collection, clazz);
    }

    public final void forEach(Consumer<T> consumer) {
        for (int i = 0; i < data.length; i++) {
            consumer.accept(data[i]);
        }
    }

    public final <U> U accumulate(U identity, BiFunction<U, T, U> consumer) {
        U result = identity;
        for (int i = 0; i < data.length; i++) {
            result = consumer.apply(result, data[i]);
        }
        return result;
    }

    public final T[] getData() {
        return data;
    }
}
