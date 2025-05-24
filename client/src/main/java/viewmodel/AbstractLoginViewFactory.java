package viewmodel;

import network.socketwrappers.SenderTypes.LoginStateSender;

public interface AbstractLoginViewFactory {
    AbstractLoginView getLoginView(RequestHandler requestHandler, LoginStateSender authenticatingSocket);
}
