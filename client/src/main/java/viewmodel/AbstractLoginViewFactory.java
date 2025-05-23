package viewmodel;

import network.socketwrappers.SenderTypes.LoginStateSender;

public interface AbstractLoginViewFactory {
    AbstractView getLoginView(RequestHandler requestHandler, LoginStateSender authenticatingSocket);
}
