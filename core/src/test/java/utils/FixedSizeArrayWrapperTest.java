package utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

public class FixedSizeArrayWrapperTest {
    private FixedSizeArrayWrapper<Integer> arrayWrapper;

    private static Collection<Integer> createAListOfAGivenSize(int size) {
        var list = new LinkedList<Integer>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    @Test
    void arrayThatIsTooBigShouldntBeAllowed() {
        var list = createAListOfAGivenSize(FixedSizeArrayWrapper.maxSize + 1);

        assertThrows(AssertionError.class, () -> {
            arrayWrapper = new FixedSizeArrayWrapper<>(list);
        });
    }

    @Test
    void arrayThatHasSizeEqualToMaxsizeShouldBeAllowed() {
        var list = createAListOfAGivenSize(FixedSizeArrayWrapper.maxSize);

        assertDoesNotThrow(() -> {
            arrayWrapper = new FixedSizeArrayWrapper<>(list);
        });
    }

    @Test
    void testWhetherTheContentsAreActuallyPresentInTheWrappedArray() {
        var list = createAListOfAGivenSize(FixedSizeArrayWrapper.maxSize);

        arrayWrapper = new FixedSizeArrayWrapper<>(list);

        assertArrayEquals(list.toArray(), arrayWrapper.getData());
    }
}
