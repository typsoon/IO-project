package utils;

import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.Iterator;

public class BoundedQueue<T> extends AbstractCollection<T> {
    private final ArrayDeque<T> deque;
    private final int maxSize;

    public BoundedQueue(int maxSize) {
        this.maxSize = maxSize;
        this.deque = new ArrayDeque<>(maxSize);
    }

    @Override
    public boolean add(T element) {
        if (maxSize <= 0)
            return false;
        if (deque.size() == maxSize) {
            deque.pollFirst(); // discard oldest
        }
        deque.addLast(element);
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    @Override
    public int size() {
        return deque.size();
    }
}
