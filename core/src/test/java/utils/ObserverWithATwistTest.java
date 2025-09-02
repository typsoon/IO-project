package utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class ObserverWithATwistTest {

    @Test
    void singleSubscriberReceivesNotification() {
        ObserverWithATwist.ObserverImpl observer = new ObserverWithATwist.ObserverImpl();

        AtomicInteger callCount = new AtomicInteger(0);
        observer.registerSubscriber(deltaTime -> callCount.incrementAndGet());

        observer.notifySubscribers(0.5f);

        assertEquals(1, callCount.get(), "Subscriber should be called once");
    }

    @Test
    void multipleSubscribersReceiveNotification() {
        ObserverWithATwist.ObserverImpl observer = new ObserverWithATwist.ObserverImpl();

        AtomicInteger callsA = new AtomicInteger(0);
        AtomicInteger callsB = new AtomicInteger(0);

        observer.registerSubscriber(deltaTime -> callsA.incrementAndGet());
        observer.registerSubscriber(deltaTime -> callsB.incrementAndGet());

        observer.notifySubscribers(1.0f);

        assertEquals(1, callsA.get(), "First subscriber should be called once");
        assertEquals(1, callsB.get(), "Second subscriber should be called once");
    }

    @Test
    void subscriberReceivesCorrectDeltaTime() {
        ObserverWithATwist.ObserverImpl observer = new ObserverWithATwist.ObserverImpl();

        AtomicInteger received = new AtomicInteger(0);
        observer.registerSubscriber(deltaTime -> received.set((int) (deltaTime * 100)));

        observer.notifySubscribers(0.75f);

        assertEquals(75, received.get(), "Subscriber should receive correct deltaTime value");
    }

    @Test
    void noSubscribersDoesNotThrow() {
        ObserverWithATwist.ObserverImpl observer = new ObserverWithATwist.ObserverImpl();

        // Just checking it runs without exception
        assertDoesNotThrow(() -> observer.notifySubscribers(0.25f));
    }

    @Test
    void concurrentNotificationIsThreadSafe() throws InterruptedException {
        ObserverWithATwist.ObserverImpl observer = new ObserverWithATwist.ObserverImpl();

        Set<Integer> results = new ConcurrentSkipListSet<>();
        int threads = 10;
        CountDownLatch latch = new CountDownLatch(threads);

        observer.registerSubscriber(deltaTime -> results.add((int) (deltaTime * 100)));

        for (int i = 0; i < threads; i++) {
            final float value = i;
            new Thread(() -> {
                observer.notifySubscribers(value);
                latch.countDown();
            }).start();
        }

        latch.await();

        assertEquals(threads, results.size(), "All threads should deliver their notifications");
    }

    @Test
    void sameSubscriberRegisteredMultipleTimesOnlyCalledOnce() {
        ObserverWithATwist.ObserverImpl observer = new ObserverWithATwist.ObserverImpl();

        AtomicInteger counter = new AtomicInteger(0);
        ObserverWithATwist.Subscriber subscriber = deltaTime -> counter.incrementAndGet();

        observer.registerSubscriber(subscriber);
        observer.registerSubscriber(subscriber);

        observer.notifySubscribers(1.0f);

        assertEquals(1, counter.get(), "Subscriber should not be duplicated in the set");
    }
}
