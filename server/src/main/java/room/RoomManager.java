package room;

import matchmaking.IMatchmakingEngine;
import matchmaking.MatchmakingParameters;
import matchmaking.lobby.LobbyMember;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequest;

public class RoomManager implements IRoomManager {
    private final IMatchmakingEngine matchmakingEngine;
    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    private int currentRoomID = 0; // for now

    public RoomManager(IMatchmakingEngine matchmakingEngine) {
        this.matchmakingEngine = matchmakingEngine;
    }

    @Override
    public RoomRequest createRoom(RoomMember user, RoomConfig roomConfig) {
        if (user.roomsUserHandle().getRoom().isPresent())
            return RoomRequest.FAILED;
        var newRoom = new Room(user, roomConfig, currentRoomID++);
        rooms.put(newRoom.roomID(), newRoom);
        user.roomsUserHandle().joinRoomCommand(newRoom);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest deleteRoom(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RoomRequest.FAILED;
        Room room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RoomRequest.NOT_AUTHORIZED;
        room.removeAllMembers();
        rooms.remove(room.roomID());
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest joinRoom(RoomMember user, Room room) {
        if (room.isFull())
            return RoomRequest.FAILED;
        if (room.roomConfig().password() != null)
            return RoomRequest.FAILED;
        room.addMember(user);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest joinRoom(RoomMember user, Room room, String password) {
        if (room.isFull())
            return RoomRequest.FAILED;
        if (!room.roomConfig().password().equals(password))
            return RoomRequest.FAILED;
        room.addMember(user);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest leaveRoom(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (room.isEmpty())
            rooms.remove(room.roomID());
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest changeAdmin(RoomMember user, RoomMember newAdmin) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RoomRequest.NOT_AUTHORIZED;
        if (!room.members().contains(newAdmin))
            return RoomRequest.NOT_AUTHORIZED;
        room.admin().changeAdmin(newAdmin.userView());
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest kickUser(RoomMember user, RoomMember kickedUser) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RoomRequest.NOT_AUTHORIZED;
        if (!room.members().contains(kickedUser))
            return RoomRequest.NOT_AUTHORIZED;
        room.removeMember(kickedUser);
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public RoomRequest createGame(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RoomRequest.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RoomRequest.NOT_AUTHORIZED;

        Collection<LobbyMember> members = room.members().stream().map(RoomMember::getLobbyMember).toList();
        matchmakingEngine.createGame(members, new MatchmakingParameters(members.size()));
        return RoomRequest.SUCCESSFUL;
    }

    @Override
    public List<Room> listRooms() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        return rooms.values().stream()
                .filter(room -> room.roomConfig().name().equals(roomName))
                .findFirst();
    }
}
