package user;

public class UserState {
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        DEFAULT,
        IN_ROOM,
        SEARCHING,
        MATCHED_PENDING_CONFIRM,
        IN_LOBBY
    }

    private State state = State.DEFAULT;
}
