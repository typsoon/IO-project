package user;

import lobby.MatchmakingEngine;
import room.Room;
import room.RoomConfig;
import room.RoomMember;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// Interface that allows communication from the user to the room
public interface UserRoomHandle {
    void createRoom(RoomConfig roomConfig);

    List<Room> getPublicRooms();

    Optional<Room> getRoom(String roomName);

    Collection<RoomMember> getRoomMembers();

    void joinRoom(Room room);

    void joinRoom(Room room, String password);

    void leaveRoom();

    void setAdmin(RoomMember newAdmin);

    void kickUser(RoomMember user);

    void deleteRoom();

    void findGame(int lobbySize, MatchmakingEngine matchmakingEngine);

    void createGame(MatchmakingEngine matchmakingEngine);
}
