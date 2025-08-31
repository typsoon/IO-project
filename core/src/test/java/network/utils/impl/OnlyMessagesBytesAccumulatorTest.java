package network.utils.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.utils.BytesAccumulator;

public class OnlyMessagesBytesAccumulatorTest {
    private BytesAccumulator bytesAccumulator;
    private TestingUtils.ReadableAndEverythingReadIndicator dataAndIndicator;

    @BeforeEach
    void prepareAccumulator() {
        bytesAccumulator = new OnlyMessagesBytesAccumulator();
    }

    void prepareData(byte[] data, int[] partition) {
        var iter = TestingUtils.partitionAndPrepareTheData(data, partition);
        dataAndIndicator = TestingUtils.mockDataProvider(iter);
    }

    @Test
    void readsUntilEmptyDoNotThrow() {
        prepareData(ExampleData.BasicExamplePureMessages.byteArr, ExampleData.BasicExamplePureMessages.partition);
        assertDoesNotThrow(() -> TestingUtils.howManyReads(bytesAccumulator, dataAndIndicator));
    }

    @Test
    void readIsCorrect() {
        prepareData(ExampleData.BasicExamplePureMessages.byteArr, ExampleData.BasicExamplePureMessages.partition);
        var readResult = assertDoesNotThrow(
                () -> TestingUtils.readAllData(bytesAccumulator, dataAndIndicator)).toArray();

        assertArrayEquals(ExampleData.BasicExamplePureMessages.whatShouldBeRead, readResult);
    }
}
