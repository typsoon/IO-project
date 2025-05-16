package network.messages.configurationstate;

import jdk.jshell.spi.ExecutionControl;
import network.messages.Message;
import network.messages.utils.DataReceiver;

import java.io.IOException;

public class CreateRoomMessage implements Message {
    private static final byte id = 10;

    public static record Payload(String roomName, String password) {
    }

    public final Payload payload;

    public CreateRoomMessage(Payload payload) {
        this.payload = payload;
    }

    @Override
    public void encodeAndWrite(DataReceiver out) throws IOException {
        throw new IllegalAccessError();
    }
}
