package database;

import game.engine.PlayerConfig;

public interface IDatabaseManager {
    public static record UserId(int id) {
    }

    UserId getUserId(String login);

    PlayerConfig getPlayerConfig(UserId id);

    String getPlayerUsername(UserId id);

    boolean addUser(String login, String password);

    boolean checkPassword(UserId uId, String password);
}
