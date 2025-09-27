package utils;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class FixedSizeArrayWrapperTest {
    private FixedSizeArrayWrapper<Integer> arrayWrapper;

    private static Collection<Integer> createAListOfAGivenSize(final int size) {
        final var list = new LinkedList<Integer>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    @Test
    void arrayThatIsTooBigShouldntBeAllowed() {
        final var list = createAListOfAGivenSize(FixedSizeArrayWrapper.maxSize + 1);

        assertThrows(AssertionError.class, () -> {
            arrayWrapper = new FixedSizeArrayWrapper<>(list, Integer.class);
        });
    }

    @Test
    void arrayThatHasSizeEqualToMaxsizeShouldBeAllowed() {
        final var list = createAListOfAGivenSize(FixedSizeArrayWrapper.maxSize);

        assertDoesNotThrow(() -> {
            arrayWrapper = new FixedSizeArrayWrapper<>(list, Integer.class);
        });
    }

    @Test
    void testWhetherTheContentsAreActuallyPresentInTheWrappedArray() {
        var list = createAListOfAGivenSize(FixedSizeArrayWrapper.maxSize);

        arrayWrapper = new FixedSizeArrayWrapper<>(list, Integer.class);

        assertArrayEquals(list.toArray(), arrayWrapper.getData());
    }
}
