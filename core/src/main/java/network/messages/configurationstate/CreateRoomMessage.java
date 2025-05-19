package network.messages.configurationstate;

import network.messages.Message;
import network.messages.utils.DataConsumer;

import java.io.IOException;

import game.Action;

public final class CreateRoomMessage extends Message {
    public static final byte id = 10;

    public static record Payload(String roomName, String password) {
    }

    public final Payload payload;

    public CreateRoomMessage(Payload payload) {
        this.payload = payload;
    }

    @Override
    public void encodeAndWrite(DataConsumer out) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public Action getAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAction'");
    }
}
