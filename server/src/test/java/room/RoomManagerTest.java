package room;

import static gameclient.rooms.RequestResult.FAILED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.IDatabaseManager.UserId;
import gameclient.rooms.RoomConfig;
import gameclient.user.UserInfo;
import matchmaking.IMatchmakingEngine;
import user.IMatchmakingUserHandle;
import user.UserState;

public class RoomManagerTest {
    private IRoomManager roomManager;
    private RoomMember user1;
    private RoomMember user2;

    private RoomMember mockRoomMember(int id, String userName) {
        return new RoomMember(new UserInfo(new UserId(id), userName),
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
    void itShouldNotThrowEvenOnDumbData() {
        var roomConfig = new RoomConfig("sdads", "", 1, false);

        assertDoesNotThrow(() -> roomManager.createRoom(user1, roomConfig));
    }

    @Test
    void creatingRoomWithTheSameNameAsAnExistingOneShouldFail() {
        var roomConfig = new RoomConfig("R1", "", 2, false);
        roomManager.createRoom(user1, roomConfig);

        assertEquals(FAILED, roomManager.createRoom(user2, roomConfig));
    }

}
