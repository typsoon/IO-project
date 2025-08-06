package network.client;

import network.client.impl.ClientSideSocketWrapperImpl;
import network.impl.ConcreteMessageDispatcher;
import network.messages.defaultmessage.ConcreteObjectDecoder;

public class ClientSideSocketWrapperFactory {
    public ClientSideSocketWrapper getClientSideSocketWrapper() {
        return new ClientSideSocketWrapperImpl(new ConcreteMessageDispatcher(), new ConcreteObjectDecoder());
    };

}
