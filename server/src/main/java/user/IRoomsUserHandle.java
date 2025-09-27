package user;

import room.Room;

import java.util.Optional;

// Interface that allows communication from the room to the user
public interface IRoomsUserHandle {
    void leaveRoomCommand();

    void joinRoomCommand(Room room);

    Optional<Room> getRoom();

    void notifyRoomChange();
}
