package game;

public interface ActionReceiver {
    void sendAction(PlayerConnector player, Action action);
}
