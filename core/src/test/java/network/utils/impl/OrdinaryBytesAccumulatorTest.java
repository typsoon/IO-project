package network.utils.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.ReadData;

public class OrdinaryBytesAccumulatorTest {
    private BytesAccumulator bytesAccumulator;
    private TestingUtils.ReadableAndEverythingReadIndicator dataAndIndicator;

    @BeforeEach
    void prepareAccumulator() {
        bytesAccumulator = new OrdinaryBytesAccumulator();
    }

    void prepareData(byte[] data, int[] partition) {
        var iter = TestingUtils.partitionAndPrepareTheData(data, partition);
        dataAndIndicator = TestingUtils.mockDataProvider(iter);
    }

    @Test
    void readsUntilEmptyDoNotThrow() {
        prepareData(ExampleData.BasicExample.byteArr, ExampleData.BasicExample.partition);
        assertDoesNotThrow(() -> TestingUtils.howManyReads(bytesAccumulator, dataAndIndicator));
    }

    @Test
    void readIsCorrect() {
        prepareData(ExampleData.BasicExample.byteArr, ExampleData.BasicExample.partition);
        Collection<ReadData> readResult = assertDoesNotThrow(
                () -> TestingUtils.readAllData(bytesAccumulator, dataAndIndicator));

        assertArrayEquals(ExampleData.BasicExample.whatShouldBeRead, readResult.toArray());
    }
}
