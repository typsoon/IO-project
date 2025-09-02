package gameclient.rooms;

import game.utility.ISendable;

public record RoomConfig(String name, String password, int maxPlayers, boolean isPublic) implements ISendable {
}
