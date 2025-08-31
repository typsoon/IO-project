package network.utils.impl;

import static network.utils.BytesAccumulator.WhatWasRead.MESSAGE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.ReadData;

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
        prepareData(ImportantExamplePureMessages.byteArr,
                ImportantExamplePureMessages.partition);
        assertDoesNotThrow(() -> TestingUtils.howManyReads(bytesAccumulator, dataAndIndicator));
    }

    @Test
    void readIsCorrect() {
        prepareData(ImportantExamplePureMessages.byteArr,
                ImportantExamplePureMessages.partition);
        var readResult = assertDoesNotThrow(
                () -> TestingUtils.readAllData(bytesAccumulator, dataAndIndicator)).toArray();

        assertArrayEquals(ImportantExamplePureMessages.whatShouldBeRead, readResult);
    }
}

final class ImportantExamplePureMessages {
    static final byte[] byteArr = { 4, 123, 32, 127, 1,
            2, 1, 2,
            3, 7, 8, 9 };
    static final int[] partition = { 3, 1, 2, byteArr.length - 6 };

    static final ReadData[] whatShouldBeRead = {
            new ReadData(MESSAGE, ByteBuffer.wrap(Arrays.copyOfRange(byteArr, 1, 5))),
            new ReadData(MESSAGE, ByteBuffer.wrap(Arrays.copyOfRange(byteArr, 6, 8))),
            new ReadData(MESSAGE, ByteBuffer.wrap(Arrays.copyOfRange(byteArr, 9, 12))),
    };
}
