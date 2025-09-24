package user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserStateTest {
    @Test
    void defaultStateIsDefault() {
        UserState userState = new UserState();
        assertEquals(UserState.State.DEFAULT, userState.getState());
    }

    @Test
    void setStateChangesState() {
        UserState userState = new UserState();
        userState.setState(UserState.State.IN_ROOM);
        assertEquals(UserState.State.IN_ROOM, userState.getState());

        userState.setState(UserState.State.SEARCHING);
        assertEquals(UserState.State.SEARCHING, userState.getState());

        userState.setState(UserState.State.MATCHED_PENDING_CONFIRM);
        assertEquals(UserState.State.MATCHED_PENDING_CONFIRM, userState.getState());

        userState.setState(UserState.State.IN_LOBBY);
        assertEquals(UserState.State.IN_LOBBY, userState.getState());
    }
}