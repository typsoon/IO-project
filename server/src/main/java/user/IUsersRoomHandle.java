package user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RequestResult;
import gameclient.rooms.UserMembershipInfo;
import room.Room;
import room.RoomMember;

// Interface that allows communication from the user to the room
public interface IUsersRoomHandle {
    RequestResult createRoomRequest(RoomConfig roomConfig);

    RequestResult deleteRoomRequest();

    RequestResult joinRoomRequest(Room room);

    RequestResult joinRoomRequest(Room room, String password);

    RequestResult leaveRoomRequest();

    RequestResult changeAdminRequest(RoomMember newAdmin);

    RequestResult kickUserRequest(RoomMember user);

    RequestResult createGameRequest();

    List<Room> getPublicRooms();

    Optional<Room> getRoom(String roomName);

    List<UserMembershipInfo> getRoomMembers();

    Optional<Room> getMyRoom();
}
