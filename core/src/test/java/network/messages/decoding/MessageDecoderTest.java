package network.messages.decoding;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.messages.Data;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.messages.gameplaystate.InventoryStateMessage;
import network.messages.utils.DataProducer;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;

class MessageDecoderTest {
    private MessageDecoder messageDecoder;
    private DataProducer dataProducer;
    private byte[] writtenBytes;
    private byte msgSize;

    @BeforeEach
    void prepare() throws IOException {
        messageDecoder = new ConcreteMessageDecoder();

        var msg = new ConcreteObjectDecoder().decodeFromRecord(Data.state);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        var dataReceiver = new OutputStreamDataReceiver(out);
        msg.encodeAndWrite(dataReceiver);

        writtenBytes = out.toByteArray();
        msgSize = writtenBytes[0];
        writtenBytes = Arrays.copyOfRange(writtenBytes, 1, writtenBytes.length);

        ByteArrayInputStream in = new ByteArrayInputStream(writtenBytes);

        dataProducer = new InputStreamDataProducer(in);
    }

    @Test
    void decodingAInventoryStateShouldntThrow() throws IOException {
        // assertDoesNotThrow(() -> messageDecoder.decodeMessage(dataProducer));
        assertDoesNotThrow(() -> messageDecoder.decodeMessage(dataProducer),
                () -> Arrays.toString(Arrays.copyOfRange(writtenBytes, 0, Math.max(writtenBytes.length, 20))));

    }

    @Test
    void decodingAPlayerInventoryStateYieldsAMessageOfCorrectType() throws IOException {
        var msg = messageDecoder.decodeMessage(dataProducer);

        assertInstanceOf(InventoryStateMessage.class, msg);
    }

    @Test
    void decodingAPlayerInventoryStateWorks() throws IOException {
        var msg = messageDecoder.decodeMessage(dataProducer);

        assertEquals(Data.state, msg.getSendable());
    }
}
