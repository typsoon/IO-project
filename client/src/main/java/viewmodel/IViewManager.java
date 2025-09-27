package viewmodel;

import java.util.Collection;
import java.util.List;

import game.gamestates.IGameState;
import network.client.ClientSideSocketWrapper;
import frontend.assetsloading.*;

//don't use outside package concerning frontend. use AbstractViewManager instead
public interface IViewManager extends IAbstractGeneralViewManager {
    void start();

    void moveToPlayView();

    void moveToMainMenu();

    void moveToSettings();

    void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper, int id);

    void moveToGameplay(ClientSideSocketWrapper clientSideSocketWrapper, int id,
            Collection<IGameState> initialGameStates);

    default void moveToGameplay(ClientSideSocketWrapper clientSideSocketWrapper, int id) {
        moveToGameplay(clientSideSocketWrapper, id, List.of());
    };

    ITextureManager getTextureManager();
}
