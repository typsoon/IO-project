package game;

import game.engine.Event;
import game.engine.GameEngine;

import java.util.*;

public class GameSessionManager implements ActionReceiver {
    private final GameEngine gameEngine;
    private final HashMap<PlayerConnector, PlayerGamesStateSender> playerGameStateSenders = new HashMap<>();
    private final HashMap<PlayerConnector, Queue<GameState>> playerGameStateQueues = new HashMap<>();

    private final Queue<Event> eventQueue = new LinkedList<>();

    private void cycle(){
        for(PlayerConnector player : playerGameStateQueues.keySet()){
            Queue<GameState> gameStates = playerGameStateQueues.get(player);
            if (!gameStates.isEmpty()) {
                player.sendGameState(gameStates);
                gameStates.clear();
            }
        }
        gameEngine.PerformCycle(eventQueue);
        eventQueue.clear();
    }

    @Override
    public void sendAction(PlayerConnector player, Action action) {
        eventQueue.add(new Event(playerGameStateSenders.get(player),action));
    }

    GameSessionManager(GameEngine gameEngine, Collection<PlayerData> players) {
        this.gameEngine = gameEngine;
        for (PlayerData player : players) {
            player.connector().subscribe(this);
            playerGameStateQueues.put(player.connector(), new LinkedList<>());
            playerGameStateSenders.put(player.connector(), gameState -> playerGameStateQueues.get(player.connector()).add(gameState));
        }
    }
}
