package viewmodel;

import network.socketwrappers.sendertypes.LoginStateSender;

public interface AbstractLoginViewFactory {
  AbstractView getLoginView(RequestHandler requestHandler, LoginStateSender authenticatingSocket);
}
