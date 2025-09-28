package network.messages.defaultmessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.messages.Data;
import network.messages.gameplaystate.InventoryStateMessage;

//TODO: Make a custom message in order to test this
class ConcreteObjectDecoderTest {
    ObjectToMessageDecoder objectDecoder;

    @BeforeEach
    void prepare() {
        objectDecoder = new ConcreteObjectDecoder();
    }

    @Test
    void decodingARecordGivesCorrectMessage() {
        final var msg = objectDecoder.decodeFromRecord(Data.state);

        assertInstanceOf(InventoryStateMessage.class, msg);
    }

    @Test
    void recordIsProperlyDecoded() {
        final var msg = objectDecoder.decodeFromRecord(Data.state);

        assertEquals(msg.getSendable(), Data.state);
    }
}
