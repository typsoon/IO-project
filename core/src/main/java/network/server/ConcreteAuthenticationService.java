package network.server;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import database.DatabaseManager;
import database.DatabaseManager.UserId;
import java.util.Set;

public class ConcreteAuthenticationService implements AuthenticationService {
    private final DatabaseManager databaseManager;
    private final Set<Integer> possibleTokenVals = new HashSet<>();
    private final Map<Integer, UserId> tokenToUserMap = new HashMap<>();
    private final Map<UserId, Set<Token>> userToTokenMap = new HashMap<>();

    public ConcreteAuthenticationService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        var randomNum = new SecureRandom().ints().iterator();
        for (int i = 0; i < 200; i++) {
            possibleTokenVals.add(randomNum.next());
        }
    }

    @Override
    public UserId getUser(int tokenVal) {
        return tokenToUserMap.get(tokenVal);
    }

    @Override
    public int renewToken(UserId userId) {
        // TODO: Make this better

        var tokenVal = possibleTokenVals.iterator().next();
        tokenToUserMap.put(tokenVal, userId);
        userToTokenMap.putIfAbsent(userId, new HashSet<>());
        var userTokens = userToTokenMap.get(userId);
        userTokens.add(new Token(tokenVal));

        return tokenVal;
    }

    @Override
    public Token tryAuth(UserId userId, String password) {
        var actualPassword = databaseManager.getPassword(userId);

        if (!Objects.equals(password, actualPassword)) {
            return null;
        }

        if (!userToTokenMap.containsKey(userId)) {
            renewToken(userId);
        }

        return userToTokenMap.get(userId).iterator().next();
    }

}
