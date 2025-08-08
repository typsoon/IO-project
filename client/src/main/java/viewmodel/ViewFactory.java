package viewmodel;

import network.client.ClientSideSocketWrapper;

public interface ViewFactory {
    View getMainMenuView();

    View getSettingsView();

    View getLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    View getPlayView();

    View getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper);
    // AbstractView getLoginView();
}
