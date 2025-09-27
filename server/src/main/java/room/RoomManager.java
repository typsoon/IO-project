package room;

import matchmaking.IMatchmakingEngine;
import matchmaking.MatchmakingParameters;
import matchmaking.lobby.LobbyMember;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RequestResult;

public class RoomManager implements IRoomManager {
    private final IMatchmakingEngine matchmakingEngine;
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private int currentRoomID = 1;

    public RoomManager(IMatchmakingEngine matchmakingEngine) {
        this.matchmakingEngine = matchmakingEngine;
    }

    @Override
    public RequestResult createRoom(RoomMember user, RoomConfig roomConfig) {
        if (rooms.containsKey(roomConfig.name())) {
            return RequestResult.FAILED;
        }

        if (user.roomsUserHandle().getRoom().isPresent())
            return RequestResult.FAILED;
        var newRoom = new Room(user, roomConfig, currentRoomID++);
        rooms.put(roomConfig.name(), newRoom);
        user.roomsUserHandle().joinRoomCommand(newRoom);
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult deleteRoom(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RequestResult.FAILED;
        Room room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RequestResult.NOT_AUTHORIZED;
        room.removeAllMembers();
        rooms.remove(room.roomConfig().name());
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult joinRoom(RoomMember user, Room room) {
        if (room.isFull())
            return RequestResult.FAILED;
        if (room.roomConfig().password() != null)
            return RequestResult.FAILED;
        room.addMember(user);
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult joinRoom(RoomMember user, Room room, String password) {
        if (room.isFull())
            return RequestResult.FAILED;
        if (!room.roomConfig().password().equals(password))
            return RequestResult.FAILED;
        room.addMember(user);
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult leaveRoom(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RequestResult.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        room.removeMember(user);
        if (room.isEmpty())
            rooms.remove(room.roomConfig().name());
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult changeAdmin(RoomMember user, RoomMember newAdmin) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RequestResult.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RequestResult.NOT_AUTHORIZED;
        if (!room.members().contains(newAdmin))
            return RequestResult.NOT_AUTHORIZED;
        room.admin().changeAdmin(newAdmin.userView());
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult kickUser(RoomMember user, RoomMember kickedUser) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RequestResult.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RequestResult.NOT_AUTHORIZED;
        if (!room.members().contains(kickedUser))
            return RequestResult.NOT_AUTHORIZED;
        room.removeMember(kickedUser);
        return RequestResult.SUCCESSFUL;
    }

    @Override
    public RequestResult createGame(RoomMember user) {
        if (user.roomsUserHandle().getRoom().isEmpty())
            return RequestResult.FAILED;
        var room = user.roomsUserHandle().getRoom().get();
        if (!room.isAdmin(user))
            return RequestResult.NOT_AUTHORIZED;

        Collection<LobbyMember> members = room.members().stream().map(RoomMember::getLobbyMember).toList();
        matchmakingEngine.createGame(members, new MatchmakingParameters(members.size()));
        return RequestResult.SUCCESSFUL;
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
