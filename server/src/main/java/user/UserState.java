package user;

public class UserState {
    public enum State {
        IDLE,
        IN_ROOM,
        SEARCHING,
        MATCHED_PENDING_CONFIRM,
        IN_LOBBY
    }

    public State state = State.IDLE;
}
