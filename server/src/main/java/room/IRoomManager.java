package room;

import java.util.List;
import java.util.Optional;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RequestResult;

public interface IRoomManager {
    RequestResult createRoom(RoomMember user, RoomConfig roomConfig);

    RequestResult deleteRoom(RoomMember user);

    RequestResult joinRoom(RoomMember user, Room room);

    RequestResult joinRoom(RoomMember user, Room room, String password);

    RequestResult leaveRoom(RoomMember user);

    RequestResult changeAdmin(RoomMember user, RoomMember newAdmin);

    RequestResult kickUser(RoomMember user, RoomMember kickedUser);

    RequestResult createGame(RoomMember user);

    List<Room> listRooms();

    Optional<Room> getRoom(String roomName);
}
