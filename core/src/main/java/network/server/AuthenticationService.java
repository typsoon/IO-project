package network.server;

import database.IDatabaseManager.UserId;

public interface AuthenticationService {

    public static record Token(int val) {
    }

    UserId getUser(int tokenVal);

    int renewToken(UserId userId);

    Token tryAuth(UserId userId, String password);
}
