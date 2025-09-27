package network.server.nio.impl.channelattchments;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

import network.messages.decoding.MessageDecoder;
import network.server.AuthenticationService;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;

public class AttachmentFactory<T extends SessionContract> {
    public SSLChannelAttachment<T> getSSLChannelAttachment(final SelectionKey key, final T clientSession) {
        return new SSLChannelAttachment<T>(key, clientSession);
    }

    public ChannelAttachment<T> getTCPChannelAttachment(final SelectionKey key,
            final AuthenticationService authenticationService,
            final SessionCreator<T> sessionCreator, final MessageDecoder messageDecoder) {
        return new TCPChannelAttachment<T>(key, authenticationService, sessionCreator, messageDecoder);
    }

    public ChannelAttachment<T> getUDPChannelAttachment(final SelectionKey selectionKey,
            final DatagramChannel datagramChannel,
            final AuthenticationService authenticationService, final SessionCreator<T> sessionCreator) {

        return new UDPChannelAttachment<T>(selectionKey,
                datagramChannel, authenticationService,
                sessionCreator);
    }
}
