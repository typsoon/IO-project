package user.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import room.Room;
import room.RoomConfig;
import room.RoomMember;
import user.IUserRoomHandle;

public class DummyUserRoomHandle implements IUserRoomHandle {

    @Override
    public void createRoom(RoomConfig roomConfig) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRoom'");
    }

    @Override
    public List<Room> getPublicRooms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPublicRooms'");
    }

    @Override
    public Optional<Room> getRoom(String roomName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoom'");
    }

    @Override
    public Collection<RoomMember> getRoomMembers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoomMembers'");
    }

    @Override
    public void joinRoom(Room room) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'joinRoom'");
    }

    @Override
    public void joinRoom(Room room, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'joinRoom'");
    }

    @Override
    public void leaveRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveRoom'");
    }

    @Override
    public void setAdmin(RoomMember newAdmin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAdmin'");
    }

    @Override
    public void kickUser(RoomMember user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'kickUser'");
    }

    @Override
    public void deleteRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoom'");
    }

}
