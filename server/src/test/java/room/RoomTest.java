package room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomInfo;
import gameclient.user.IUserView;
import user.IRoomsUserHandle;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomTest {

    Room room;
    RoomConfig config;
    RoomMember adminMember;
    IUserView adminView;
    RoomMember member1;
    IUserView memberView1;
    RoomMember member2;
    IUserView memberView2;

    @BeforeEach
    void setUp() {
        adminView = mock(IUserView.class);
        config = mock(RoomConfig.class);
        when(config.name()).thenReturn("TestRoom");
        when(config.isPublic()).thenReturn(true);
        when(config.maxPlayers()).thenReturn(2);
        when(config.password()).thenReturn(null);

        adminMember = mock(RoomMember.class);
        when(adminMember.userView()).thenReturn(adminView);
        when(adminMember.roomsUserHandle()).thenReturn(mock(IRoomsUserHandle.class));

        room = new Room(adminMember, config, 1);

        memberView1 = mock(IUserView.class);
        member1 = mock(RoomMember.class);
        when(member1.userView()).thenReturn(memberView1);
        when(member1.roomsUserHandle()).thenReturn(mock(IRoomsUserHandle.class));

        memberView2 = mock(IUserView.class);
        member2 = mock(RoomMember.class);
        when(member2.userView()).thenReturn(memberView2);
        when(member2.roomsUserHandle()).thenReturn(mock(IRoomsUserHandle.class));
    }

    @Test
    void getRoomInfo() {
        RoomInfo info = room.getRoomInfo();
        assertEquals(1, info.roomID());
        assertEquals("TestRoom", info.roomName());
        assertTrue(info.isPublic());
        assertEquals(1, info.numberOfMembers());
        assertEquals(2, info.maxMembers());
        assertFalse(info.hasPassword());
    }

    @Test
    void hasPassword() {
        assertFalse(room.hasPassword());
        when(config.password()).thenReturn("secret");
        assertTrue(new Room(adminMember, config, 2).hasPassword());
    }

    @Test
    void setAdmin() {
        room.addMember(member1);
        room.setAdmin(member1);
        assertTrue(room.isAdmin(member1));
    }

    @Test
    void isAdmin() {
        assertTrue(room.isAdmin(adminMember));
        room.addMember(member1);
        assertFalse(room.isAdmin(member1));
    }

    @Test
    void addMember() {
        room.addMember(member1);
        assertTrue(room.members().contains(member1));
        verify(member1.roomsUserHandle(), times(1)).joinRoomCommand(room);
        room.addMember(member2);
        assertFalse(room.members().contains(member2)); // Room is full
        verify(member2.roomsUserHandle(), never()).joinRoomCommand(room);
    }

    @Test
    void removeMember() {
        room.addMember(member1);
        room.removeMember(member1);
        assertFalse(room.members().contains(member1));
        verify(member1.roomsUserHandle(), times(1)).leaveRoomCommand();
    }

    @Test
    void removeAllMembers() {
        room.addMember(member1);
        assertTrue(room.members().contains(member1));
        assertTrue(room.members().contains(adminMember));
        room.removeAllMembers();
        assertTrue(room.members().isEmpty());
        verify(adminMember.roomsUserHandle(), times(1)).leaveRoomCommand();
        verify(member1.roomsUserHandle(), times(1)).leaveRoomCommand();
    }

    @Test
    void isFull() {
        assertFalse(room.isFull());
        room.addMember(member1);
        assertTrue(room.isFull());
    }

    @Test
    void isEmpty() {
        assertFalse(room.isEmpty());
        room.removeMember(adminMember);
        assertTrue(room.isEmpty());
    }

    @Test
    void roomID() {
        assertEquals(1, room.roomID());
    }

    @Test
    void members() {
        assertEquals(1, room.members().size());
        room.addMember(member1);
        assertEquals(2, room.members().size());
    }

    @Test
    void admin() {
        assertEquals(adminView, room.admin().admin());
        room.addMember(member1);
        room.setAdmin(member1);
        assertEquals(memberView1, room.admin().admin());
    }

    @Test
    void roomConfig() {
        assertEquals(config, room.roomConfig());
    }
}