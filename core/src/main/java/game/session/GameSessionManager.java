package game.session;

import game.actions.IAction;
import game.engine.Event;
import game.engine.IGameEngine;
import game.gamestates.IGameState;
import utils.IDisposable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSessionManager implements IActionReceiver, IDisposable {
    private IGameEngine gameEngine;
    private final HashMap<ISubscribablePlayerConnector, IPlayerGamesStateSender> playerGameStateSenders = new HashMap<>();
    private final HashMap<ISubscribablePlayerConnector, Queue<IGameState>> playerGameStateQueues = new HashMap<>();

    private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>(); // TOdo rethink this, maybe use a more
    // sophisticated approach

    private static final int CYCLE_TIME = 15625; // microseconds 64 ticks in second
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void sendAction(ISubscribablePlayerConnector player, IAction action) {
        eventQueue.add(new Event(playerGameStateSenders.get(player), action));
        eventQueue.add(new Event(playerGameStateSenders.get(player), action));
    }

    public void startGameLoop() {
        scheduler.scheduleAtFixedRate(this::cycle, 0, CYCLE_TIME, TimeUnit.MICROSECONDS);
    }

    public void stopGameLoop() {
        scheduler.shutdown();
    }

    private void cycle() {
        try {
            for (ISubscribablePlayerConnector player : playerGameStateQueues.keySet()) {
                Queue<IGameState> gameStates = playerGameStateQueues.get(player);
                if (!gameStates.isEmpty()) {
                    // System.out.println("Sending game states to player: " + player);
                    player.sendGameStates(gameStates);
                    gameStates.clear();
                }
            }
            Collection<Event> eventsToProcess = new ArrayList<>(eventQueue);
            Event event;
            while ((event = eventQueue.poll()) != null) {
                eventsToProcess.add(event);
            }
            gameEngine.performCycle(eventsToProcess);
        } catch (Exception e) {
            // TODO: handle exceptions properly, maybe log them
            e.printStackTrace();
        }
    }

    protected Map<ISubscribablePlayerConnector, IPlayerGamesStateSender> getPlayerGameStateSenders() {
        return Collections.unmodifiableMap(playerGameStateSenders);
    }

    protected void setupEngine(IGameEngine gameEngine) {
        this.gameEngine = gameEngine;
        startGameLoop();
    }

    protected GameSessionManager(Collection<PlayerData> players) {
        for (PlayerData player : players) {
            player.connector().subscribe(this);
            playerGameStateQueues.put(player.connector(), new LinkedList<>());
            playerGameStateSenders.put(player.connector(),
                    gameState -> playerGameStateQueues.get(player.connector()).add(gameState));
        }
    }

    @Override
    public void dispose() {
        stopGameLoop();
        for (ISubscribablePlayerConnector playerConnector : playerGameStateSenders.keySet()) {
            playerConnector.unsubscribe(this);
        }
        gameEngine.dispose();
    }

    // for testing purposes
    public IGameEngine getGameEngine() {
        return gameEngine;

    }
}
