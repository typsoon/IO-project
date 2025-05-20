package room;

public record RoomConfig(String name, String password, int maxPlayers, boolean isPublic) {
}
