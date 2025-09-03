package gameclient.rooms;

import utils.ISendable;

public record UserMembershipInfo(String roomName, int userID, String username, boolean isAdmin) implements ISendable {

}
