package network.utils.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import network.utils.BytesAccumulator.Readable;
import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.ReadData;

final class TestingUtils {
    private final static int seed = 29;
    private final static int mod = 37;

    static byte[] concatArrays(byte[]... arrays) {
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

    private static byte[] pseudoRandomByteSeq(int len) {
        byte[] answer = new byte[len];

        int prev = seed;
        for (int i = 0; i < answer.length; i++) {
            answer[i] = (byte) ((prev * seed) % mod);
            prev = answer[i];
        }

        return answer;
    }

    private static final byte[][] partition(byte[] data, int[] sizes) {
        int total = 0;
        for (int s : sizes) {
            total += s;
        }
        if (total != data.length) {
            throw new IllegalArgumentException(
                    "Sum of sizes (" + total + ") does not equal data length (" + data.length + ")");
        }

        byte[][] result = new byte[sizes.length][];
        int offset = 0;
        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            byte[] part = new byte[size];
            System.arraycopy(data, offset, part, 0, size);
            result[i] = part;
            offset += size;
        }

        return result;
    }

    public static final Iterator<ByteBuffer> partitionAndPrepareTheData(final byte[] data, final int[] sizes) {
        var totalLen = 0;
        for (int size : sizes) {
            totalLen += size;
        }
        assert totalLen == data.length;

        final var parts = partition(data, sizes);

        return new Iterator<ByteBuffer>() {
            private int pos = 0;
            private ByteBuffer actByteBuffer = null;

            @Override
            public boolean hasNext() {
                return pos < parts.length;
            }

            @Override
            public ByteBuffer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                if (actByteBuffer == null) {
                    actByteBuffer = ByteBuffer.wrap(parts[pos]);
                    return actByteBuffer;
                }

                if (actByteBuffer.hasRemaining()) {
                    return actByteBuffer;
                }

                pos++;
                actByteBuffer = null;

                return hasNext() ? next() : ByteBuffer.allocate(0);
            }
        };
    }

    public record ReadableAndEverythingReadIndicator(Readable readable, Supplier<Boolean> wasEverythingRead) {
    }

    private final static class BoolWrapper {
        private boolean boolVal;

        public BoolWrapper(boolean boolVal) {
            this.boolVal = boolVal;
        }

        public void setBoolVal(boolean boolVal) {
            this.boolVal = boolVal;
        }

        public boolean isBoolVal() {
            return boolVal;
        }
    }

    public final static ReadableAndEverythingReadIndicator mockDataProvider(Iterator<ByteBuffer> dataInParts) {
        final BoolWrapper wasEverythingRead = new BoolWrapper(false);

        Readable readable = byteBuffer -> {
            if (!dataInParts.hasNext()) {
                wasEverythingRead.setBoolVal(true);
                return 0;
            }

            ByteBuffer actByteBuf = dataInParts.next();

            final int res = Math.min(byteBuffer.remaining(), actByteBuf.remaining());

            final int oldLimit = actByteBuf.limit();
            actByteBuf.limit(actByteBuf.position() + res);
            byteBuffer.put(actByteBuf);
            actByteBuf.limit(oldLimit);

            return res;
        };

        return new ReadableAndEverythingReadIndicator(readable, wasEverythingRead::isBoolVal);
    }

    public static int howManyReads(BytesAccumulator bytesAccumulator,
            TestingUtils.ReadableAndEverythingReadIndicator dataAndIndicator) throws IOException {
        int answer = 0;

        while (!dataAndIndicator.wasEverythingRead().get()) {
            bytesAccumulator.accumulateBytes(dataAndIndicator.readable());

            answer++;
        }

        return answer;
    }

    public static Collection<BytesAccumulator.ReadData> readAllData(BytesAccumulator bytesAccumulator,
            TestingUtils.ReadableAndEverythingReadIndicator dataAndIndicator) throws IOException {
        Collection<ReadData> answer = new LinkedList<>();
        while (!dataAndIndicator.wasEverythingRead().get()) {
            var res = bytesAccumulator.accumulateBytes(dataAndIndicator.readable());
            if (!res.isEmpty()) {
                answer.add(res.get());
            }
        }

        return answer;
    }
}
