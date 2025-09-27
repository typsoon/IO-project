package network.server.nio.impl.channelattchments;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import network.messages.Message;
import network.messages.utils.ByteChannelDataReceiver;
import network.messages.utils.DataConsumer;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.socketwrappers.SocketTypes.SocketSender;

abstract class ConnectionBasedChannelAttachment<T extends SessionContract, U extends Message>
        extends ChannelAttachmentTraits<T> {
    protected final Queue<Message> msgQueue = new ConcurrentLinkedQueue<>();
    protected final SelectionKey key;
    private final DataConsumer consumer;

    ConnectionBasedChannelAttachment(final SelectionKey key) {
        this.consumer = new ByteChannelDataReceiver((WritableByteChannel) key.channel());
        this.key = key;
    }

    @Override
    public final void dispatchMessages() throws IOException {
        synchronized (key) {
            for (final Message msg : msgQueue) {
                // Logger.getGlobal().info("I AM HERE AND SENDING
                // %s".formatted(msg.getSendable()));

                msg.encodeAndWrite(consumer);

                // Logger.getGlobal()
                // .info(() -> "Sending message %s with payload
                // %s".formatted(msg.getClass().getSimpleName(),
                // msg.getSendable()));
            }
            msgQueue.clear();

            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    public final SocketSender<U> getSender() {
        return message -> {
            synchronized (key) {
                Logger.getGlobal().finest("Message %s enqueued".formatted(message.getSendable()));

                msgQueue.add(message);
                key.interestOpsOr(SelectionKey.OP_WRITE);

            }

            key.selector().wakeup();
        };
    }
}
