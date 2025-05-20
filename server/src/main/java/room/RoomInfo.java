package room;

public record RoomInfo(String roomName, boolean isPublic, int numberOfMembers, int maxMembers, boolean hasPassword) {
}
