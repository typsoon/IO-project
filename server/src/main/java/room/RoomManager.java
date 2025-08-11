package room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomManager implements IRoomManager {
    private final List<Room> rooms = new ArrayList<>();

    @Override
    public RoomRequest createRoom(RoomMember user, RoomConfig roomConfig) {
        if (user.roomsUserHandle().getRoom().isPresent()) return RoomRequest.FAILED;
        var newRoom = new Room(user, roomConfig);
        rooms.add(newRoom);
        user.roomsUserHandle().joinRoomCommand(newRoom);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest deleteRoom(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty()) return RoomRequest.FAILED;
        Room room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user)) return RoomRequest.NOT_AUTHORIZED;
        room.removeAllMembers();
        rooms.remove(room);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest joinRoom(RoomMember user, Room room) {
        if (room.isFull()) return RoomRequest.FAILED;
        if (room.roomConfig().password() != null) return RoomRequest.FAILED;
        room.addMember(user);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest joinRoom(RoomMember user, Room room, String password) {
        if (room.isFull()) return RoomRequest.FAILED;
        if (!room.roomConfig().password().equals(password)) return RoomRequest.FAILED;
        room.addMember(user);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest leaveRoom(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty()) return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (room.isEmpty()) rooms.remove(room);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest changeAdmin(RoomMember user, RoomMember newAdmin) {
        if (user.roomsUserHandle().getRoom().isEmpty()) return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user)) return RoomRequest.NOT_AUTHORIZED;
        if (!room.members().contains(newAdmin)) return RoomRequest.NOT_AUTHORIZED;
        room.admin().changeAdmin(newAdmin.userInfo());
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest kickUser(RoomMember user, RoomMember kickedUser) {
        if (user.roomsUserHandle().getRoom().isEmpty()) return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user)) return RoomRequest.NOT_AUTHORIZED;
        if (!room.members().contains(kickedUser)) return RoomRequest.NOT_AUTHORIZED;
        room.removeMember(kickedUser);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public List<Room> listRooms() {
        return rooms;
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        return rooms.stream().filter(room -> room.roomConfig().name().equals(roomName)).findFirst();
    }
}
