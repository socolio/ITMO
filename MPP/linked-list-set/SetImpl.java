package linked_list_set;

import kotlinx.atomicfu.AtomicRef;


public class SetImpl implements Set {

    private class Node {

        AtomicRef<Object> next;
        int x;

        Node(int x, Object next) {
            this.next = new AtomicRef<>(next);
            this.x = x;
        }

        Node nextNode() {
            Object n = next.getValue();

            if (n instanceof Node) {
                return (Node) n;
            } else {
                return ((Removed) n).next;
            }
        }
    }

    private class Removed {

        final Node next;

        Removed(Node next) {
            this.next = next;
        }
    }

    private class Window {
        Node cur, next;
    }

    private final Node head = new Node(Integer.MIN_VALUE, new Node(Integer.MAX_VALUE, null));

    /**
     * Returns the {@link Window}, where cur.x < x <= next.x
     */
    private Window findWindow(int x) {
        cas: while (true) {
            Window w = new Window();

            w.cur = head;
            w.next = w.cur.nextNode();

            while (w.next.x < x) {
                Object nr = w.next.next.getValue();
                if (nr instanceof Removed) {
                    if (!w.cur.next.compareAndSet(w.next, ((Removed) nr).next)) {
                        if(w.cur.next.getValue() instanceof Removed || w.next.next.getValue() instanceof Removed){
                            continue cas;
                        }
                        continue;
                    }

                    w.next = ((Removed) nr).next;
                } else {
                    w.cur = w.next;
                    w.next = w.cur.nextNode();
                }
            }

            Object nn = w.next.next.getValue();
            if (nn instanceof Removed) {
                w.cur.next.compareAndSet(w.next, ((Removed) nn).next);
                continue;
            }

            return w;
        }
    }

    @Override
    public boolean add(int x) {
        while (true) {
            Window w = findWindow(x);
            if (w.next.x == x) {
                return false;
            }

            Node newNode = new Node(x, w.next);
            if (w.cur.next.compareAndSet(w.next, newNode)) {
                return true;
            }
        }
    }

    @Override
    public boolean remove(int x) {
        while (true) {
            Window w = findWindow(x);
            if (w.next.x != x) {
                return false;
            }

            Node nextNode = w.next.nextNode();
            if (w.next.next.compareAndSet(nextNode, new Removed(nextNode))) {
                w.cur.next.compareAndSet(w.next, nextNode);
                return true;
            }
        }
    }


    @Override
    public boolean contains(int x) {
        Window w = findWindow(x);
        return w.next.x == x;
    }
}