package network.server.nio.impl.channelattchments;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;

import network.messages.Message.EncryptedMessage;
import network.messages.utils.ByteBufferDataProducer;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.utils.AccumulatorAdapters;
import network.utils.BytesAccumulator;
import network.utils.BytesAccumulator.Readable;
import network.utils.BytesAccumulator.WhatWasRead;
import network.utils.impl.OnlyMessagesBytesAccumulator;

public class SSLChannelAttachment<T extends SessionContract>
        extends ConnectionBasedChannelAttachment<T, EncryptedMessage> {
    // TODO: think about this
    private final BytesAccumulator bytesAccumulator = new OnlyMessagesBytesAccumulator();
    private final Readable clientSocketChannel;
    private final T clientSession;

    SSLChannelAttachment(final SelectionKey key, final T clientSession) {
        super(key);
        this.clientSocketChannel = AccumulatorAdapters
                .getRedableByteChannelAdapter((ReadableByteChannel) key.channel());
        this.clientSession = clientSession;
    }

    @Override
    public List<ClientAndTheirMessage<T>> readIncomingMessages() throws IOException {
        final var readRes = bytesAccumulator.accumulateBytes(clientSocketChannel);
        final List<ClientAndTheirMessage<T>> answer = new LinkedList<>();

        if (readRes.isEmpty()) {
            return answer;
        }

        final var data = readRes.get();
        assert data.whatWasRead() == WhatWasRead.MESSAGE;

        final var msg = messageDecoder.decodeMessage(new ByteBufferDataProducer(data.byteBuf()));
        answer.add(new ClientAndTheirMessage<T>(clientSession, msg));

        return answer;
    }

}
