package gameclient.user;

import database.IDatabaseManager.UserId;

public interface IUserView {
    UserId id();

    String username();
}
