package viewmodel;

import network.client.ClientSideSocketWrapper;

public interface AbstractViewFactory {
    AbstractView getMainMenuView();

    AbstractView getSettingsView();

    AbstractView getLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    AbstractView getPlayView();

    AbstractView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper);
    // AbstractView getLoginView();
}
