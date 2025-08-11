package user;

import database.IDatabaseManager.UserId;

public record UserInfo(UserId id, String username) {
}
