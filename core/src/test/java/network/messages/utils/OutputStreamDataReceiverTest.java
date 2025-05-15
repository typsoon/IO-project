package network.messages.utils;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OutputStreamDataReceiverTest {
    private OutputStreamDataReceiver testedReceiver;
    private ByteArrayOutputStream out;

    private void prepareDataTableAndOut() {
        out = new ByteArrayOutputStream();
        testedReceiver = new OutputStreamDataReceiver(out);
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
        void getByteDoesntThrowForNoReason() {
            assertDoesNotThrow(() -> testedReceiver.putByte((byte) 42),
                    "Put byte should not throw without a reason");
        }

        @Test
        void getByteGivesCorrectByte() {
            getByteDoesntThrowForNoReason();
            assertArrayEquals(new byte[]{42}, out.toByteArray());
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
            assertArrayEquals(CorrectlyEncodedData.encodedInteger, out.toByteArray());
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
            assertArrayEquals(CorrectlyEncodedData.encodedString, out.toByteArray());
        }
    }
}
