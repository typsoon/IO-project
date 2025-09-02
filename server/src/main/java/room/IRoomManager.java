package room;

import java.util.List;
import java.util.Optional;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequest;

public interface IRoomManager {
    RoomRequest createRoom(RoomMember user, RoomConfig roomConfig);

    RoomRequest deleteRoom(RoomMember user);

    RoomRequest joinRoom(RoomMember user, Room room);

    RoomRequest joinRoom(RoomMember user, Room room, String password);

    RoomRequest leaveRoom(RoomMember user);

    RoomRequest changeAdmin(RoomMember user, RoomMember newAdmin);

    RoomRequest kickUser(RoomMember user, RoomMember kickedUser);

    RoomRequest createGame(RoomMember user);

    List<Room> listRooms();

    Optional<Room> getRoom(String roomName);
}
