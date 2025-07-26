package network.impl;

import java.util.Objects;

import network.messages.Message;
import network.socketwrappers.SocketTypes.SocketSender;

public class SingleWriteSocketContainer<T extends Message, U extends SocketSender<T>> {
    private U socketWrapper = null;

    public void setSocketWrapper(U socketSender) {
        if (this.socketWrapper != null) {
            throw new IllegalStateException("Element already set to %s".formatted(this.socketWrapper));
        }
        this.socketWrapper = socketSender;
    }

    SocketSender<T> getSocketWrapper() {
        return Objects.requireNonNull(socketWrapper);
    }
}
