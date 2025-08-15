package user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import room.IRoomManager.RoomRequest;
import room.Room;
import room.RoomConfig;
import room.RoomMember;

// Interface that allows communication from the user to the room
public interface IUsersRoomHandle {
    RoomRequest createRoomRequest(RoomConfig roomConfig);

    RoomRequest deleteRoomRequest();

    RoomRequest joinRoomRequest(Room room);

    RoomRequest joinRoomRequest(Room room, String password);

    RoomRequest leaveRoomRequest();

    RoomRequest changeAdminRequest(RoomMember newAdmin);

    RoomRequest kickUserRequest(RoomMember user);

    RoomRequest createGameRequest();

    List<Room> getPublicRooms();

    Optional<Room> getRoom(String roomName);

    Collection<RoomMember> getRoomMembers();
}
