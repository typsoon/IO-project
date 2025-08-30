package network.messages.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.utility.Point2F;
import game.utility.Vector2F;

public class ByteBufferDataProducerTest {
    private ByteBufferDataProducer testedProducer;
    private ByteArrayInputStream in;

    private void prepareDataTableAndIn(byte[] bytesToBeRead) {
        var data = new byte[2 * bytesToBeRead.length + 4];
        var byteBuf = ByteBuffer.wrap(data);

        var randomBytes = new byte[bytesToBeRead.length + 4];
        new SecureRandom().nextBytes(randomBytes);

        byteBuf.put(bytesToBeRead);
        byteBuf.put(randomBytes);

        byteBuf.flip();

        // var readableByteStream = mock(ReadableByteChannel.class);

        testedProducer = assertDoesNotThrow(() -> new ByteBufferDataProducer(byteBuf),
                "Initializing producer shouldn't throw for no reason");
    }

    @Nested
    class getByteTests {
        @BeforeEach
        void init() {
            prepareDataTableAndIn(new byte[] { 42 });
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

    @Nested
    class getPoint2FTests {
        @BeforeEach
        void init() {
            prepareDataTableAndIn(CorrectlyEncodedData.encodedPoint2F);
        }

        // @Disabled
        // @Test
        // void getStringShouldThrowOnInvalidBuffer() {
        //
        // }
        //
        @Test
        void getPoint2FDoesntThrowForNoReason() {
            assertDoesNotThrow(testedProducer::getPoint2F,
                    "Get string should not throw without a reason");
        }

        @Test
        void getPoint2FGivesCorrectResult() throws IOException {
            Point2F decodedPoint2F = testedProducer.getPoint2F();
            assertEquals(CorrectlyEncodedData.point2F, decodedPoint2F);
        }
    }

    @Nested
    class getVector2FTests {
        @BeforeEach
        void init() {
            prepareDataTableAndIn(CorrectlyEncodedData.encodedVector2F);
        }

        @Test
        void getVector2FDoesntThrowForNoReason() {
            assertDoesNotThrow(testedProducer::getVector2F,
                    "Get vector should not throw without a reason");
        }

        @Test
        void getVector2FGivesCorrectResult() throws IOException {
            Vector2F decodedVector2F = testedProducer.getVector2F();
            assertEquals(CorrectlyEncodedData.vector2F, decodedVector2F);
        }
    }
}
