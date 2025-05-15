package network.messages.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputStreamDataProducerTest {
    private InputStreamDataProducer testedProducer;
    private ByteArrayInputStream in;

    private void prepareDataTableAndIn(byte[] bytesToBeRead) {
        var data = new byte[2 * bytesToBeRead.length + 4];
        var byteBuf = ByteBuffer.wrap(data);

        var randomBytes = new byte[bytesToBeRead.length + 4];
        new SecureRandom().nextBytes(randomBytes);

        byteBuf.put(bytesToBeRead);
        byteBuf.put(randomBytes);

        in = new ByteArrayInputStream(data);
        testedProducer = new InputStreamDataProducer(in);
    }

    @Nested
    class getByteTests {
        @BeforeEach
        void init() {
            prepareDataTableAndIn(new byte[]{42});
        }

        // @Disabled
        // @Test
        // void getByteShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void getByteDoesntThrowForNoReason() {
            assertDoesNotThrow(testedProducer::getByte,
                    "Get byte should not throw without a reason");
        }

        @Test
        void getByteGivesCorrectByte() throws IOException {
            var decodedByte = testedProducer.getByte();
            assertEquals(42, decodedByte);
        }
    }

    @Nested
    class getIntTests {

        @BeforeEach
        void init() {
            prepareDataTableAndIn(CorrectlyEncodedData.encodedInteger);
        }

        // @Disabled
        // @Test
        // void getIntShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void getIntDoesntThrowForNoReason() {
            assertDoesNotThrow(testedProducer::getInt,
                    "Get string should not throw without a reason");
        }

        @Test
        void getIntGivesCorrectInt() throws IOException {
            int decodedInt = testedProducer.getInt();
            assertEquals(CorrectlyEncodedData.integer, decodedInt);
        }
    }

    @Nested
    class getStringTests {
        @BeforeEach
        void init() {
            prepareDataTableAndIn(CorrectlyEncodedData.encodedString);
        }

        // @Disabled
        // @Test
        // void getStringShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void getStringDoesntThrowForNoReason() {
            assertDoesNotThrow(testedProducer::getString,
                    "Get string should not throw without a reason");
        }

        @Test
        void getStringGivesCorrectString() throws IOException {
            String decodedString = testedProducer.getString();
            assertEquals(CorrectlyEncodedData.string, decodedString);
        }
    }
}
