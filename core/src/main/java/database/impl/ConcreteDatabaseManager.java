package database.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import database.IDatabaseManager;
import network.utils.Credentials;

public class ConcreteDatabaseManager implements IDatabaseManager {
    private final Set<Integer> possibleIdVals;

    private static record FullUserData(UserId userId, Credentials credentials) {
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
    }

    @Override
    public UserId getUserId(String login) {
        return users.stream().filter(user -> user.credentials().login().equals(login))
                .findFirst()
                .map(FullUserData::userId)
                .orElse(null);
    }

    @Override
    public void addUser(String login, String password) {
        var id = possibleIdVals.iterator().next();
        possibleIdVals.remove(id);

        users.add(new FullUserData(new UserId(id), new Credentials(login, password)));
    }

    @Override
    public String getPassword(UserId uId) {
        return users.stream().filter(uData -> uData.userId.equals(uId))
                .map(uData -> uData.credentials.password())
                .findFirst()
                .orElse(null);
    }

}
