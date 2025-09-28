package database.impl;

import database.IDatabaseManager;
import game.engine.PlayerConfig;
import network.utils.Credentials;

import java.util.*;
import java.util.logging.Logger;

public class ConcreteDatabaseManager implements IDatabaseManager {
    private final Set<Integer> possibleIdVals;

    private static record FullUserData(UserId userId, Credentials credentials, PlayerConfig playerConfig) {
    }

    private final Collection<FullUserData> users;

    public ConcreteDatabaseManager() {
        possibleIdVals = new HashSet<>();
        users = new ArrayList<>();

        var intStreamIter = new Random().ints().iterator();

        for (int i = 0; i < 100; i++) {
            possibleIdVals.add(intStreamIter.next());
        }

        addUser("u1", "p1");
        addUser("u2", "p2");
        addUser("u3", "p3");
        addUser("u", "p");
    }

    @Override
    public UserId getUserId(String login) {
        return users.stream().filter(user -> user.credentials().login().equals(login))
                .findFirst()
                .map(FullUserData::userId)
                .orElse(null);
    }

    @Override
    public PlayerConfig getPlayerConfig(UserId uId) {
        return users.stream().filter(uData -> uData.userId.equals(uId))
                .map(FullUserData::playerConfig)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getPlayerUsername(UserId uId) {
        return users.stream().filter(uData -> uData.userId.equals(uId))
                .map(uData -> uData.credentials.login())
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addUser(String login, String password) {
        if (login == null || password == null) {
            Logger.getGlobal().info("Null credentials");
            return false;
        }

        if (!possibleIdVals.iterator().hasNext()) {
            Logger.getGlobal().info("There are no available ids");
            return false;
        }

        if (getUserId(login) != null) {
            Logger.getGlobal().info("Login unavailable");
            return false;
        }

        var id = possibleIdVals.iterator().next();
        possibleIdVals.remove(id);

        users.add(new FullUserData(new UserId(id), new Credentials(login, password), new PlayerConfig()));
        return true;
    }

    @Override
    public boolean checkPassword(UserId uId, String password) {
        var optional = users.stream().filter(uData -> uData.userId.equals(uId))
                .map(uData -> uData.credentials.password())
                .findFirst();
        if (optional.isEmpty()) {
            return false;
        }
        String actualPassword = optional.get();
        return actualPassword.equals(password);
    }
}
