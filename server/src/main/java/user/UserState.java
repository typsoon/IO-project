package user;

public class UserState {
    public enum State {
        DEFAULT,
        IN_ROOM,
        IN_QUEUE,
        IN_GAME
    }

    public State state = State.DEFAULT;
}
