package network.messages.decoding;

import java.io.IOException;

import network.messages.Message;
import network.messages.utils.DataProducer;

public interface MessageDecoder {
    Message decodeMessage(DataProducer in) throws IOException;
}
