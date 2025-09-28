package network.server;

import database.IDatabaseManager;
import database.IDatabaseManager.UserId;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcreteAuthenticationService implements AuthenticationService {
    private final IDatabaseManager databaseManager;
    private final Set<Integer> possibleTokenVals = ConcurrentHashMap.newKeySet();
    private final Map<Integer, UserId> tokenToUserMap = new ConcurrentHashMap<>();
    private final Map<UserId, Set<Token>> userToTokenMap = new ConcurrentHashMap<>();

    public ConcreteAuthenticationService(IDatabaseManager databaseManager) {
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
    public synchronized int renewToken(UserId userId) {
        // TODO: Make this better

        var tokenIter = possibleTokenVals.iterator();
        var tokenVal = tokenIter.next();
        tokenIter.remove();

        tokenToUserMap.put(tokenVal, userId);
        userToTokenMap.putIfAbsent(userId, new HashSet<>());
        var userTokens = userToTokenMap.get(userId);
        userTokens.add(new Token(tokenVal));

        return tokenVal;
    }

    @Override
    public synchronized Token tryAuth(UserId userId, String password) {
        if (!databaseManager.checkPassword(userId, password)) {
            return null;
        }

        if (!userToTokenMap.containsKey(userId)) {
            renewToken(userId);
        }

        return userToTokenMap.get(userId).iterator().next();
    }

}
