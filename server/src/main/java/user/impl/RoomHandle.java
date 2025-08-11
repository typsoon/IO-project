package user.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import room.IRoomManager;
import room.IRoomManager.RoomRequest;
import room.Room;
import room.RoomConfig;
import room.RoomMember;
import user.IRoomsUserHandle;
import user.IMatchmakingUserHandle;
import user.IUsersRoomHandle;
import user.UserInfo;

public class RoomHandle implements IUsersRoomHandle, IRoomsUserHandle {
    private final IRoomManager roomManager;
    private final RoomMember member;
    private Room room = null;

    RoomHandle(IRoomManager roomManager, UserInfo userInfo, IMatchmakingUserHandle matchmakingUserHandle) {
        this.roomManager = roomManager;
        this.member = new RoomMember(userInfo, matchmakingUserHandle, this);
    }

    @Override
    public void leaveRoomCommand() {
        this.room = null;
    }

    @Override
    public void joinRoomCommand(Room room) {
        this.room = room;
    }

    @Override
    public Optional<Room> getRoom() {
        return Optional.ofNullable(room);
    }

    @Override
    public RoomRequest createRoomRequest(RoomConfig roomConfig) {
        return roomManager.createRoom(member, roomConfig);
    }

    @Override
    public RoomRequest deleteRoomRequest() {
        return roomManager.deleteRoom(member);
    }

    @Override
    public RoomRequest joinRoomRequest(Room room) {
        return roomManager.joinRoom(member, room);
    }

    @Override
    public RoomRequest joinRoomRequest(Room room, String password) {
        return roomManager.joinRoom(member, room, password);
    }

    @Override
    public RoomRequest leaveRoomRequest() {
        return roomManager.leaveRoom(member);
    }

    @Override
    public RoomRequest changeAdminRequest(RoomMember newAdmin) {
        return roomManager.changeAdmin(member, newAdmin);
    }

    @Override
    public RoomRequest kickUserRequest(RoomMember user) {
        return roomManager.kickUser(member, user);
    }

    @Override
    public List<Room> getPublicRooms() {
        return roomManager.listRooms().stream().filter(room -> room.roomConfig().isPublic()).collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        return roomManager.getRoom(roomName);
    }

    @Override
    public Collection<RoomMember> getRoomMembers() {
        if (room == null) return List.of();
        return room.members();
    }
}
