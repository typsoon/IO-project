package network.client;

import network.client.impl.ClientSideSocketWrapperImpl;
import network.impl.ConcreteMessageDispatcher;

public class ClientSideSocketWrapperFactory {
    public ClientSideSocketWrapper getClientSideSocketWrapper() {
        return new ClientSideSocketWrapperImpl(new ConcreteMessageDispatcher());
    };

}
