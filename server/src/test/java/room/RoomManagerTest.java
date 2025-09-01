package room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static room.IRoomManager.RoomRequest.FAILED;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import database.IDatabaseManager.UserId;
import gameclient.rooms.RoomConfig;
import matchmaking.IMatchmakingEngine;
import user.IMatchmakingUserHandle;
import user.IUserView;
import user.UserState;

public class RoomManagerTest {
    private IRoomManager roomManager;
    private RoomMember user1;
    private RoomMember user2;

    private record UserData(UserId id, String username) implements IUserView {
    }

    private RoomMember mockRoomMember(int id, String userName) {
        return new RoomMember(new UserData(new UserId(id), userName),
                mock(IMatchmakingUserHandle.class),
                mock(),
                new UserState());
    }

    @BeforeEach
    void prepareForTests() {
        var matchmakingEngine = mock(IMatchmakingEngine.class);
        roomManager = new RoomManager(matchmakingEngine);

        user1 = mockRoomMember(1, "u1");
        user2 = mockRoomMember(2, "u2");
    }

    @Test
    void emptyOptionalShouldBeReturnedWhenAskingForANonexistentRoom() {
        assertEquals(Optional.empty(), roomManager.getRoom("R1"));
    }

    @Test
    @Disabled
    void creatingRoomWithTheSameNameAsAnExistingOneShouldFail() {
        var roomConfig = new RoomConfig("R1", "", 2, false);
        roomManager.createRoom(user1, roomConfig);

        assertEquals(FAILED, roomManager.createRoom(user2, roomConfig));
    }
}
