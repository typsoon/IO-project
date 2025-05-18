package network.messages.defaultmessage;

import java.io.IOException;

import game.Action;
import network.messages.Message;
import network.messages.utils.DataReceiver;

public abstract class DefaultMessage<T extends Action> extends Message {
    private final T action;

    protected DefaultMessage(T action) {
        this.action = action;
    }

    @Override
    public void encodeAndWrite(DataReceiver out) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAction'");
    }

    @Override
    public Action getAction() {
        return action;
    }
}
