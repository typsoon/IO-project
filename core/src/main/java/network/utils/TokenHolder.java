package network.utils;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread
 * safe container for
 * the token
 */
public class TokenHolder implements TokenView {
    private Optional<Integer> token = Optional.empty();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void setToken(int tokenVal) {
        if (!token.isEmpty()) {
            throw new IllegalStateException("You are setting a token for the second time");
        }

        token = Optional.of(tokenVal);
    }

    public void renewToken(int tokenVal) {
        lock.writeLock().lock();
        try {
            token = Optional.of(tokenVal);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getToken() {
        lock.readLock().lock();
        try {
            return token.orElseThrow();
        } finally {
            lock.readLock().unlock();
        }
    }
}
