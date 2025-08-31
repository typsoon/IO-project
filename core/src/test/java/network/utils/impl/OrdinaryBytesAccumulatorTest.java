package network.utils.impl;

import static network.utils.BytesAccumulator.WhatWasRead.MESSAGE;
import static network.utils.BytesAccumulator.WhatWasRead.TOKEN;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.ByteBuffer;

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
        prepareData(BasicExampleWToken.byteArr, BasicExampleWToken.partition);
        assertDoesNotThrow(() -> TestingUtils.howManyReads(bytesAccumulator, dataAndIndicator));
    }

    @Test
    void readIsCorrect() {
        prepareData(BasicExampleWToken.byteArr, BasicExampleWToken.partition);
        var readResult = assertDoesNotThrow(
                () -> TestingUtils.readAllData(bytesAccumulator, dataAndIndicator)).toArray();

        assertArrayEquals(BasicExampleWToken.whatShouldBeRead, readResult);
    }
}

final class BasicExampleWToken {
    private static final byte[] tokenPart = { 1, 3, 0, 35 };
    private static final byte[] msgPart = { 100, 126, 121, 12, 123 };
    private static final byte[] msgPartLenByteArr = { (byte) msgPart.length };

    static final byte[] byteArr = TestingUtils.concatArrays(tokenPart, msgPartLenByteArr, msgPart);
    static final int[] partition = { 3, 1, 2, byteArr.length - 6 };

    static final ReadData[] whatShouldBeRead = {
            new ReadData(TOKEN, ByteBuffer.wrap(tokenPart)),
            new ReadData(MESSAGE, ByteBuffer.wrap(msgPart)),
    };
}
