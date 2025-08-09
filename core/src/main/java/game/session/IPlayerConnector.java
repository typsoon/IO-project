package game.session;

import java.util.Collection;

import game.gamestates.IGameState;

@FunctionalInterface
public interface IPlayerConnector {
    void sendGameStates(Collection<IGameState> gameStates);
}
