package network.utils.impl;

import static network.utils.BytesAccumulator.WhatWasRead.MESSAGE;
import static network.utils.BytesAccumulator.WhatWasRead.TOKEN;

import java.nio.ByteBuffer;
import java.util.Arrays;

import network.utils.BytesAccumulator.ReadData;

public class ExampleData {
    // static interface DataExample {
    // ReadData[] getWhatShouldBeRead();
    //
    // byte[] getByteArr();
    //
    // int[] getPartition();
    // }

    private static byte[] concatArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] arr : arrays) {
            totalLength += arr.length;
        }

        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] arr : arrays) {
            System.arraycopy(arr, 0, result, offset, arr.length);
            offset += arr.length;
        }

        return result;
    }

    static final class BasicExampleWToken {
        private static final byte[] tokenPart = { 1, 3, 0, 35 };
        private static final byte[] msgPart = { 100, 126, 121, 12, 123 };
        private static final byte[] msgPartLenByteArr = { (byte) msgPart.length };

        static final byte[] byteArr = concatArrays(tokenPart, msgPartLenByteArr, msgPart);
        static final int[] partition = { 3, 1, 2, byteArr.length - 6 };

        static final ReadData[] whatShouldBeRead = {
                new ReadData(TOKEN, ByteBuffer.wrap(tokenPart)),
                new ReadData(MESSAGE, ByteBuffer.wrap(msgPart)),
        };
    }

    static final class BasicExamplePureMessages {
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
}
