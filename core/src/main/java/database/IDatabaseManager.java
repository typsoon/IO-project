package database;

import game.engine.PlayerConfig;

public interface IDatabaseManager {
    public static record UserId(int id) {
    }

    UserId getUserId(String login);

    PlayerConfig getPlayerConfig(UserId id);

    String getPlayerUsername(UserId id);

    void addUser(String login, String password);

    // TODO: change this to get passwordHash
    String getPassword(UserId uId);
}
