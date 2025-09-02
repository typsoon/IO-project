package gameclient.rooms;

import game.utility.ISendable;

public record UserMembershipInfo(String roomName, int userID, String username) implements ISendable {

}
