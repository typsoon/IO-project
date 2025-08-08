package database;

public interface IDatabaseManager {
    public static record UserId(int id) {
    }

    UserId getUserId(String login);

    void addUser(String login, String password);

    // TODO: change this to get passwordHash
    String getPassword(UserId uId);
}
