// server/src/test/java/user/impl/RoomHandleTest.java
package user.impl;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RequestResult;
import gameclient.user.IUserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import room.IRoomManager;
import room.Room;
import room.RoomMember;
import user.IMatchmakingUserHandle;
import user.UserState;
import utils.ObserverWithATwist;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomHandleTest {

    private IRoomManager roomManager;
    private IUserView userView;
    private IMatchmakingUserHandle matchmakingUserHandle;
    private UserState userState;
    private RoomHandle roomHandle;
    private final ObserverWithATwist.Notifiable notifier = mock(ObserverWithATwist.Notifiable.class);

    @BeforeEach
    void setUp() {
        roomManager = mock(IRoomManager.class);
        userView = mock(IUserView.class);
        matchmakingUserHandle = mock(IMatchmakingUserHandle.class);
        userState = new UserState();
        roomHandle = new RoomHandle(roomManager, userView, matchmakingUserHandle, userState, notifier);
    }

    @Test
    void leaveRoomCommand_clearsRoomAndSetsDefaultState() {
        Room room = mock(Room.class);
        roomHandle.joinRoomCommand(room);
        roomHandle.leaveRoomCommand();
        assertEquals(Optional.empty(), roomHandle.getRoom());
        assertEquals(UserState.State.DEFAULT, userState.getState());
    }

    @Test
    void joinRoomCommand_setsRoomAndState() {
        Room room = mock(Room.class);
        roomHandle.joinRoomCommand(room);
        assertEquals(Optional.of(room), roomHandle.getRoom());
        assertEquals(UserState.State.IN_ROOM, userState.getState());
    }

    @Test
    void getRoom_returnsCurrentRoomOrEmpty() {
        assertEquals(Optional.empty(), roomHandle.getRoom());
        Room room = mock(Room.class);
        roomHandle.joinRoomCommand(room);
        assertEquals(Optional.of(room), roomHandle.getRoom());
    }

    @Test
    void createRoomRequest_delegatesToManager() {
        RoomConfig config = mock(RoomConfig.class);
        when(roomManager.createRoom(any(), eq(config))).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.createRoomRequest(config));
        verify(roomManager, times(1)).createRoom(any(), eq(config));
    }

    @Test
    void deleteRoomRequest_delegatesToManager() {
        when(roomManager.deleteRoom(any())).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.deleteRoomRequest());
        verify(roomManager, times(1)).deleteRoom(any());
    }

    @Test
    void joinRoomRequest_delegatesToManager() {
        Room room = mock(Room.class);
        when(roomManager.joinRoom(any(), eq(room))).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.joinRoomRequest(room));
        verify(roomManager, times(1)).joinRoom(any(), eq(room));
    }

    @Test
    void joinRoomRequestWithPassword_delegatesToManager() {
        Room room = mock(Room.class);
        String password = "pass";
        when(roomManager.joinRoom(any(), eq(room), eq(password))).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.joinRoomRequest(room, password));
        verify(roomManager, times(1)).joinRoom(any(), eq(room), eq(password));
    }

    @Test
    void leaveRoomRequest_delegatesToManager() {
        when(roomManager.leaveRoom(any())).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.leaveRoomRequest());
        verify(roomManager, times(1)).leaveRoom(any());
    }

    @Test
    void changeAdminRequest_delegatesToManager() {
        RoomMember newAdmin = mock(RoomMember.class);
        when(roomManager.changeAdmin(any(), eq(newAdmin))).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.changeAdminRequest(newAdmin));
        verify(roomManager, times(1)).changeAdmin(any(), eq(newAdmin));
    }

    @Test
    void kickUserRequest_delegatesToManager() {
        RoomMember user = mock(RoomMember.class);
        when(roomManager.kickUser(any(), eq(user))).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.kickUserRequest(user));
        verify(roomManager, times(1)).kickUser(any(), eq(user));
    }

    @Test
    void createGameRequest_delegatesToManager() {
        when(roomManager.createGame(any())).thenReturn(RequestResult.SUCCESSFUL);
        assertEquals(RequestResult.SUCCESSFUL, roomHandle.createGameRequest());
        verify(roomManager, times(1)).createGame(any());
    }

    @Test
    void getPublicRooms_filtersOnlyPublicRooms() {
        RoomConfig publicConfig = mock(RoomConfig.class);
        when(publicConfig.isPublic()).thenReturn(true);
        RoomConfig privateConfig = mock(RoomConfig.class);
        when(privateConfig.isPublic()).thenReturn(false);

        Room publicRoom = mock(Room.class);
        Room privateRoom = mock(Room.class);
        when(publicRoom.roomConfig()).thenReturn(publicConfig);
        when(privateRoom.roomConfig()).thenReturn(privateConfig);

        when(roomManager.listRooms()).thenReturn(List.of(publicRoom, privateRoom));
        List<Room> result = roomHandle.getPublicRooms();
        assertEquals(List.of(publicRoom), result);
    }

    @Test
    void getRoomByName_delegatesToManager() {
        Room room = mock(Room.class);
        when(roomManager.getRoom("test")).thenReturn(Optional.of(room));
        assertEquals(Optional.of(room), roomHandle.getRoom("test"));
        verify(roomManager, times(1)).getRoom("test");
    }

    @Test
    void getRoomMembers_returnsEmptyListIfNoRoom() {
        assertTrue(roomHandle.getRoomMembers().isEmpty());
    }

//    @Test
//    void getRoomMembers_returnsMembersIfRoomSet() {
//        Room room = mock(Room.class);
//        RoomMember member1 = mock(RoomMember.class);
//        RoomMember member2 = mock(RoomMember.class);
//        when(room.members()).thenReturn(List.of(member1, member2));
//        roomHandle.joinRoomCommand(room);
//
//        assertEquals(List.of(member1, member2), roomHandle.getRoomMembers());
//    }
}