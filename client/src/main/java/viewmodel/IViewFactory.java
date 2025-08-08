package viewmodel;

import network.client.ClientSideSocketWrapper;

public interface IViewFactory {
    IView getMainMenuView();

    IView getSettingsView();

    IView getLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    IView getPlayView();

    IView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper);
    // AbstractView getLoginView();
}
