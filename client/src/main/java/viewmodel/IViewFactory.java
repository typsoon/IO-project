package viewmodel;

import java.util.Collection;

import game.gamestates.IGameState;
import network.client.ClientSideSocketWrapper;

public interface IViewFactory {
    IView getMainMenuView();

    IView getSettingsView();

    IView getLoginView(ClientSideSocketWrapper clientSideSocketWrapper);

    IView getPlayView();

    IView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper, int id);

    IView getGameplayView(ClientSideSocketWrapper clientSideSocketWrapper, int id,
            Collection<IGameState> initialGameStates);
    // AbstractView getLoginView();
}
