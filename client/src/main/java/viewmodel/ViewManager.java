package viewmodel;

import network.client.ClientSideSocketWrapper;

//don't use outside package concerning frontend. use AbstractViewManager instead
public interface ViewManager extends AbstractGeneralViewManager {
    void start();

    void moveToPlayView();

    void moveToMainMenu();

    void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper);

    ViewFactory getViewFactory();

    TextureManager getTextureManager();
}
