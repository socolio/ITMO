package msqueue;

import kotlinx.atomicfu.AtomicRef;

public class MSQueue implements Queue {

    private AtomicRef<Node> head;
    private AtomicRef<Node> tail;


    public MSQueue() {
        Node dummy = new Node(0);
        this.head = new AtomicRef<>(dummy);
        this.tail = new AtomicRef<>(dummy);
    }

    @Override
    public void enqueue(int x) {
        Node newTail = new Node(x);
        while (true) {
            Node t = tail.getValue();
            if (t.next.compareAndSet(null, newTail)) {
                tail.compareAndSet(t, newTail);
                return;
            } else {
                tail.compareAndSet(t, t.next.getValue());
            }
        }
    }

    @Override
    public int dequeue() {
        return dequeue(true);
    }

    @Override
    public int peek() {
        return dequeue(false);
    }

    private int dequeue(boolean remove){
        while (true) {
            Node h = head.getValue();
            Node t = tail.getValue();

            Node hNext = h.next.getValue();

            if (h == t) {
                if (hNext == null) {
                    return Integer.MIN_VALUE;
                } else {
                    tail.compareAndSet(t, hNext);
                }
            } else {
                if (head.compareAndSet(h, remove ? hNext : h)) {
                    return hNext.x;
                }
            }
        }
    }

    private class Node {
        final int x;
        AtomicRef<Node> next = new AtomicRef<>(null);

        Node(int x) {
            this.x = x;
        }
    }
}