package network.messages.defaultmessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import network.messages.Message;

public class ConcreteObjectDecoder implements ObjectToMessageDecoder {
    private static final Map<Class<?>, Function<Object, ? extends Message>> recordDecoders = new HashMap<>();
    static {
        recordDecoders.putAll(GeneratedClassesData.recordDecoders);
    }

    @Override
    public Message decodeFromRecord(Object record) {
        return recordDecoders.get(record).apply(record);
    }
}
