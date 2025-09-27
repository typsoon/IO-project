package network.utils;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TokenHolderTest {
    private TokenHolder holder;

    @BeforeEach
    void setUp() {
        holder = new TokenHolder();
    }

    @Test
    void testSetTokenOnce() {
        holder.setToken(42);
        assertEquals(42, holder.getToken());
    }

    @Test
    void testSetTokenTwiceThrows() {
        holder.setToken(10);
        assertThrows(IllegalStateException.class, () -> holder.setToken(20));
    }

    @Test
    void testRenewTokenOverridesValue() {
        holder.setToken(5);
        holder.renewToken(99);
        assertEquals(99, holder.getToken());
    }

    @Test
    void testConcurrentAccess() throws Exception {
        holder.setToken(1);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            // Readers
            Callable<Integer> reader = holder::getToken;
            // Writer
            Runnable writer = () -> holder.renewToken(2);

            // Submit many readers + a writer
            var futures = IntStream.range(0, 10)
                    .mapToObj(i -> executor.submit(reader))
                    .toList();

            executor.submit(writer).get();

            for (Future<Integer> f : futures) {
                int value = f.get();
                assertTrue(value == 1 || value == 2,
                        "Token must be old or renewed value, but was " + value);
            }

            // After writer finishes, token must be new
            assertEquals(2, holder.getToken());
        } finally {
            executor.shutdown();
        }
    }
}
