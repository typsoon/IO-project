package network.messages.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import network.messages.MessagesConfig;

public class ByteBufferDataReceiverTest {
    private DataConsumer testedReceiver;
    private ByteBuffer out;

    private void prepareDataTableAndOut() {
        out = ByteBuffer.allocate(MessagesConfig.maxUdpPacketLength);
        testedReceiver = new ByteBufferDataConsumer(out);
    }

    private final byte[] getAsBytes() {
        out.flip();
        byte[] writtenBytes = new byte[out.remaining()];
        out.get(writtenBytes);

        return writtenBytes;
    }

    @Nested
    class getByteTests {
        @BeforeEach
        void init() {
            prepareDataTableAndOut();
        }

        // @Disabled
        // @Test
        // void getByteShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void putByteDoesntThrowForNoReason() {
            assertDoesNotThrow(() -> testedReceiver.putByte((byte) 42),
                    "Put byte should not throw without a reason");
        }

        @Test
        void putByteGivesCorrectByte() {
            putByteDoesntThrowForNoReason();

            assertArrayEquals(new byte[] { 42 }, getAsBytes());
        }
    }

    @Nested
    class getIntTests {

        @BeforeEach
        void init() {
            prepareDataTableAndOut();
        }

        // @Disabled
        // @Test
        // void getIntShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void putIntDoesntThrowForNoReason() {
            assertDoesNotThrow(() -> testedReceiver.putInt(CorrectlyEncodedData.integer),
                    "Put int should not throw without a reason");
        }

        @Test
        void getIntGivesCorrectInt() {
            putIntDoesntThrowForNoReason();
            assertArrayEquals(CorrectlyEncodedData.encodedInteger, getAsBytes());
        }
    }

    @Nested
    class getStringTests {
        @BeforeEach
        void init() {
            prepareDataTableAndOut();
        }

        // @Disabled
        // @Test
        // void getStringShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void putStringDoesntThrowForNoReason() {
            assertDoesNotThrow(() -> testedReceiver.putString(CorrectlyEncodedData.string),
                    "Get string should not throw without a reason");
        }

        @Test
        void getStringGivesCorrectString() {
            putStringDoesntThrowForNoReason();
            assertArrayEquals(CorrectlyEncodedData.encodedString, getAsBytes());
        }
    }
}
