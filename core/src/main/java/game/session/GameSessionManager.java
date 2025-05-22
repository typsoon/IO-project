package game.session;

import game.*;
import game.actions.Action;
import game.engine.Event;
import game.engine.GameEngine;
import game.gamestates.GameState;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameSessionManager implements ActionReceiver, Closeable {
    private GameEngine gameEngine;
    private final HashMap<PlayerConnector, PlayerGamesStateSender> playerGameStateSenders = new HashMap<>();
    private final HashMap<PlayerConnector, Queue<GameState>> playerGameStateQueues = new HashMap<>();

    private final Queue<Event> eventQueue = new LinkedList<>();

    private static final int CYCLE_TIME = 15625; // milliseconds 64 ticks in second
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void sendAction(PlayerConnector player, Action action) {
        eventQueue.add(new Event(playerGameStateSenders.get(player),action));
    }

    public void startGameLoop(){
        scheduler.scheduleAtFixedRate(this::cycle, 0, CYCLE_TIME, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void stopGameLoop(){
        scheduler.shutdown();
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
    @Override
    public void close() throws  java.io.IOException {
        stopGameLoop();
        for (PlayerConnector playerConnector : playerGameStateSenders.keySet()) {
            playerConnector.unsubscribe(this);
        }
        gameEngine.close();
    }

}
