package network.messages.defaultmessage;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import game.actions.Action;
import messagetraits.AutoMessageTraits;
import network.messages.Message;
import network.messages.utils.DataConsumer;
import network.messages.utils.DataProducer;

@AutoMessageTraits(consumer = DataConsumer.class, producer = DataProducer.class)
public abstract class DefaultMessage<T extends Action> extends Message {

    public static Map<Byte, Function<DataProducer, ? extends Message>> decoders = Collections
            .unmodifiableMap(GeneratedClassesData.decoders);
    public static Map<Class<?>, Function<Object, ? extends Message>> recordDecoders = Collections
            .unmodifiableMap(GeneratedClassesData.recordDecoders);
}

record PositionRecord(int i, byte b, int y) implements Action {
};

abstract class Position extends DefaultMessage<PositionRecord> {
    public static final byte id = 20;
}
