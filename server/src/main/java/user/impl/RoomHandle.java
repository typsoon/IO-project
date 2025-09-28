package user.impl;

import gameclient.rooms.UserMembershipInfo;
import room.IRoomManager;
import room.Room;
import room.RoomMember;
import user.*;
import user.UserState.State;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RequestResult;
import gameclient.user.IUserView;
import utils.ObserverWithATwist;

public class RoomHandle implements IUsersRoomHandle, IRoomsUserHandle {
    private final IRoomManager roomManager;
    private final RoomMember member;
    private Room room = null;
    private final ObserverWithATwist.Notifiable notifier;

    RoomHandle(IRoomManager roomManager, IUserView userView,
            IMatchmakingUserHandle matchmakingUserHandle, UserState userState, ObserverWithATwist.Notifiable notifier) {
        this.notifier = notifier;
        this.roomManager = roomManager;
        this.member = new RoomMember(userView, matchmakingUserHandle, this, userState);
    }

    @Override
    public void leaveRoomCommand() {
        this.room = null;
        this.member.userState().setState(State.DEFAULT);
    }

    @Override
    public void joinRoomCommand(Room room) {
        this.room = room;
        this.member.userState().setState(State.IN_ROOM);
    }

    @Override
    public Optional<Room> getRoom() {
        return Optional.ofNullable(room);
    }

    @Override
    public void notifyRoomChange() {
        notifier.notifySubscribers(0);
    }

    @Override
    public RequestResult createRoomRequest(RoomConfig roomConfig) {
        return roomManager.createRoom(member, roomConfig);
    }

    @Override
    public RequestResult deleteRoomRequest() {
        return roomManager.deleteRoom(member);
    }

    @Override
    public RequestResult joinRoomRequest(Room room) {
        return roomManager.joinRoom(member, room);
    }

    @Override
    public RequestResult joinRoomRequest(Room room, String password) {
        return roomManager.joinRoom(member, room, password);
    }

    @Override
    public RequestResult leaveRoomRequest() {
        return roomManager.leaveRoom(member);
    }

    @Override
    public RequestResult changeAdminRequest(RoomMember newAdmin) {
        return roomManager.changeAdmin(member, newAdmin);
    }

    @Override
    public RequestResult kickUserRequest(RoomMember user) {
        return roomManager.kickUser(member, user);
    }

    @Override
    public RequestResult createGameRequest() {
        return roomManager.createGame(member);
    }

    @Override
    public List<Room> getPublicRooms() {
        return roomManager.listRooms().stream().filter(room -> room.roomConfig().isPublic())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        return roomManager.getRoom(roomName);
    }

    @Override
    public List<UserMembershipInfo> getRoomMembers() {
        if (room == null)
            return List.of();
        return room.members().stream()
                .map(member -> new UserMembershipInfo(room.roomConfig().name(), member.userView().id().id(),
                        member.userView().username(), member.userView().id().equals(room.admin().admin().id())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getMyRoom() {
        return Optional.ofNullable(room);
    }
}
