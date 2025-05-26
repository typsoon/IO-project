package room;

import java.util.List;
import java.util.Optional;

public interface IRoomManager {
    Optional<Room> createRoom(RoomConfig roomConfig, RoomMember user);

    Optional<Room> getRoom(String roomName);

    void deleteRoom(Room room);

    List<Room> listRooms();
}
