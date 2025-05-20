package network.messages.defaultmessage;

import game.Action;
import messagetraits.AutoMessageTraits;
import network.messages.Message;
import network.messages.utils.DataConsumer;
import network.messages.utils.DataProducer;

@AutoMessageTraits(consumer = DataConsumer.class, producer = DataProducer.class)
public abstract class DefaultMessage<T extends Action> extends Message {
}

record PositionRecord(int i, byte b, int y) implements Action {
};

abstract class Position extends DefaultMessage<PositionRecord> {
    public static final byte id = 20;
}
