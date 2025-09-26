package utils;

import org.junit.jupiter.api.Test;

public class BoundedQueueTest {
    @Test
    public void singleElementQueue() {
        var queue = new BoundedQueue<Integer>(1);

        var it = queue.iterator();
        assert (!it.hasNext());

        queue.add(2137);
        it = queue.iterator();
        assert (it.hasNext());
        assert (it.next() == 2137);
        assert (!it.hasNext());

        queue.add(420);
        it = queue.iterator();
        assert (it.hasNext());
        assert (it.next() == 420);
        assert (!it.hasNext());
        assert (queue.size() == 1);
    }

    @Test
    public void multipleElementQueue() {
        int testSize = 10;
        var queue = new BoundedQueue<Integer>(testSize);

        for (int i = 0; i < testSize; i++)
            queue.add(i);
        assert (queue.size() == testSize);
        var it = queue.iterator();
        assert (it.hasNext());
        assert (it.next() == 0);

        queue.add(1);
        assert (queue.size() == testSize);
        it = queue.iterator();
        assert (it.hasNext());
        assert (it.next() == 1);
    }

    @Test
    public void invalidMaxSizeZero() {
        var queue = new BoundedQueue<Integer>(0);

        queue.add(37);
        var it = queue.iterator();
        assert (!it.hasNext());
    }

    @Test
    public void invalidMaxSizeNegative() {
        var queue = new BoundedQueue<Integer>(-1);

        queue.add(42);
        var it = queue.iterator();
        assert (!it.hasNext());
    }
}
