package game.session;

import game.*;
import game.engine.Event;
import game.engine.GameEngine;

import java.util.*;

public class GameSessionManager implements ActionReceiver {
    private GameEngine gameEngine;
    private final HashMap<PlayerConnector, PlayerGamesStateSender> playerGameStateSenders = new HashMap<>();
    private final HashMap<PlayerConnector, Queue<GameState>> playerGameStateQueues = new HashMap<>();

    private final Queue<Event> eventQueue = new LinkedList<>();

    @Override
    public void sendAction(PlayerConnector player, Action action) {
        eventQueue.add(new Event(playerGameStateSenders.get(player),action));
    }

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

    protected Map<PlayerConnector, PlayerGamesStateSender> getPlayerGameStateSenders() {
        return Collections.unmodifiableMap(playerGameStateSenders);
    }

    protected void SetupEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    protected GameSessionManager(Collection<PlayerData> players) {
        for (PlayerData player : players) {
            player.connector().subscribe(this);
            playerGameStateQueues.put(player.connector(), new LinkedList<>());
            playerGameStateSenders.put(player.connector(), gameState -> playerGameStateQueues.get(player.connector()).add(gameState));
        }
    }


}
