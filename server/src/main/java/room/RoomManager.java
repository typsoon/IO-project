package room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomManager implements IRoomManager {
    private final List<Room> rooms = new ArrayList<>();

    @Override
    public Optional<Room> createRoom(RoomConfig roomConfig, RoomMember user) {
        if (getRoom(roomConfig.name()).isPresent()) {
            return Optional.empty();
        }
        Room room = new Room(user, roomConfig);
        rooms.add(room);
        return Optional.of(room);
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        return rooms.stream()
                .filter(room -> room.getRoomInfo().roomName().equals(roomName))
                .findFirst();
    }

    @Override
    public void deleteRoom(Room room) {
        for (var member : room.members()) {
            room.removeMember(member);
        }
        rooms.remove(room);
    }

    @Override
    public List<Room> listRooms() {
        return rooms;
    }
}
