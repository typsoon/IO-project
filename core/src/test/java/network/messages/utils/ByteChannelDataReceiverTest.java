package network.messages.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.junit.jupiter.api.BeforeEach;

public class ByteChannelDataReceiverTest {
    private ByteChannelDataReceiver testedReceiver;
    private ByteArrayOutputStream out;

    private void prepareDataTableAndOut() {
        out = new ByteArrayOutputStream();
        var channelMock = mock(WritableByteChannel.class);

        assertDoesNotThrow(() -> doAnswer(invocation -> {
            ByteBuffer byteBufArg = invocation.getArgument(0);
            // out.write(byteBufArg.compact().array());
            var bytes = new byte[byteBufArg.remaining()];
            byteBufArg.get(bytes);
            out.write(bytes);
            return bytes.length;
        }).when(channelMock).write(any(ByteBuffer.class)), "Mocking shouldn't throw");

        testedReceiver = new ByteChannelDataReceiver(channelMock);
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
            assertDoesNotThrow(() -> testedReceiver.putByte((byte) 42), "Put byte should not throw without a reason");
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
            assertDoesNotThrow(() -> testedReceiver.putInt(CorrectlyEncodedData.integer), "Put int should not throw without a reason");
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
            assertDoesNotThrow(() -> testedReceiver.putString(CorrectlyEncodedData.string), "Get string should not throw without a reason");
        }

        @Test
        void getStringGivesCorrectString() {
            putStringDoesntThrowForNoReason();
            assertArrayEquals(CorrectlyEncodedData.encodedString, out.toByteArray());
        }
    }
}
