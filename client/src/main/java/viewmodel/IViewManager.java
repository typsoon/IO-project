package viewmodel;

import network.client.ClientSideSocketWrapper;

//don't use outside package concerning frontend. use AbstractViewManager instead
public interface IViewManager extends IAbstractGeneralViewManager {
    void start();

    void moveToPlayView();

    void moveToMainMenu();

    void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper);

    ITextureManager getTextureManager();
}
