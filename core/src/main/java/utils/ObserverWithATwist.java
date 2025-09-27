package utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ObserverWithATwist {
    private ObserverWithATwist() {
    }

    public static interface Subscribable {
        void registerSubscriber(Subscriber subscriber);
        // void unregisterSubscriber(Subscriber subscriber);
    }

    public static interface Notifiable {
        void notifySubscribers(float deltaTime);
    }

    @FunctionalInterface
    public static interface Subscriber {
        void notifySubscriber(float deltaTime);
    }

    public static final class ObserverImpl implements Subscribable, Notifiable {
        private final Set<Subscriber> registeredSubscribers = ConcurrentHashMap.newKeySet();

        @Override
        public final void registerSubscriber(Subscriber subscriber) {
            registeredSubscribers.add(subscriber);
        }

        // @Override
        // public final void unregisterSubscriber(Subscriber subscriber) {
        // registeredSubscribers.remove(subscriber);
        // }

        @Override
        public void notifySubscribers(float deltaTime) {
            registeredSubscribers.forEach(sub -> sub.notifySubscriber(deltaTime));
        }
    }

}
