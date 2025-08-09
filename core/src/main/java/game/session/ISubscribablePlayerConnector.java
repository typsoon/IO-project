package game.session;

public interface ISubscribablePlayerConnector extends IPlayerConnector {
    void subscribe(IActionReceiver receiver);

    void unsubscribe(IActionReceiver receiver);
}
