package user;

public interface UserRoomHandler {
    void createRoom();

    void deleteRoom();

    void kickMember(UserHandle user);

    void leaveRoom();

    void joinRoom(String roomName); // TODO: some id would be nice or smth idk
}
