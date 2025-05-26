package user;

import lobby.IMatchmakingEngine;
import room.Room;
import room.RoomConfig;
import room.IRoomManager;
import room.RoomMember;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RoomHandle implements IUserRoomHandle, IRoomUserHandle {
    private final IRoomManager roomManager;
    private final IUserHandle user;
    private Room room = null;
    private final RoomMember member;

    public RoomHandle(IRoomManager roomManager, IUserHandle user) {
        this.roomManager = roomManager;
        this.user = user;
        this.member = new RoomMember(user, this);
    }

    @Override
    public void createRoom(RoomConfig roomConfig) {
        if (room != null) return;
        roomManager.createRoom(roomConfig, member).ifPresentOrElse(
                r -> room = r,
                () -> {
                    // Handle room creation failure
                    System.out.println("Room creation failed");
                }
        );
    }

    @Override
    public List<Room> getPublicRooms() {
        return roomManager.listRooms().stream()
                .filter(room -> room.getRoomInfo().isPublic())
                .toList();
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        return roomManager.getRoom(roomName);
    }

    @Override
    public Collection<RoomMember> getRoomMembers() {
        return room != null ? room.members() : List.of();
    }

    @Override
    public void joinRoom(Room room) {
        if (this.room != null) return;
        if (room.hasPassword()) return; // TODO: handle message back
        this.room = room;
        room.addMember(member);
    }

    @Override
    public void joinRoom(Room room, String password) {
        if (this.room != null) return;
        if (!room.roomConfig().password().equals(password)) return; // TODO: handle message back
        this.room = room;
        room.addMember(member); // TODO: Check password
    }

    @Override
    public void leaveRoom() {
        if (room == null) return;
        room.removeMember(member);
        if (room.members().isEmpty()) {
            roomManager.deleteRoom(room);
        }
        room = null;
    }

    @Override
    public void setAdmin(RoomMember newAdmin) {
        if (room == null) return;
        if (room.getAdmin() != this.user) return;
        room.setAdmin(newAdmin);
    }

    @Override
    public void kickUser(RoomMember user) {
        if (room == null) return;
        if (room.getAdmin() != this.user) return;
        room.removeMember(user);
    }

    @Override
    public void deleteRoom() {
        if (room == null) return;
        if (room.getAdmin() != this.user) return;
        roomManager.deleteRoom(room);
    }

    @Override
    public void findGame(int lobbySize, IMatchmakingEngine matchmakingEngine) {
        if (room == null || room.getAdmin() != this.user) return;
        if (room.members().size() > lobbySize) return;
        matchmakingEngine.findGame(room.members().stream().map(RoomMember::user).toList(), lobbySize);
    }

    @Override
    public void createGame(IMatchmakingEngine matchmakingEngine) {
        if (room == null || room.getAdmin() != this.user) return;
        matchmakingEngine.findGame(room.members().stream().map(RoomMember::user).toList(), room.members().size());
    }
}
