package session.receivers;

public class GameplayStateConsumerFactory {
    public GameplayStateConsumer getGameplayStateConsumer() {
        return new GameplayStateConsumer();
    }

}
