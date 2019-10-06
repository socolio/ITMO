package stack;

import kotlinx.atomicfu.AtomicRef;

import java.util.Random;

public class StackImpl implements Stack {

    private static class Node {
        final Node next;
        final int x;

        Node(int x, Node next) {
            this.next = next;
            this.x = x;
        }
    }

    private AtomicRef<Node> head = new AtomicRef<>(null);

    private AtomicRef<Node>[] elimination = new AtomicRef[8];
    private Node done = new Node(0, null);
    private Random rnd = new Random();

    public StackImpl() {
        for (int i = 0; i < elimination.length; i++) {
            elimination[i] = new AtomicRef<>(null);
        }
    }

    @Override
    public void push(int x) {
        int i = rnd.nextInt(elimination.length);
        for (int j = 0; j <= 6; j++) {
            int a = i;
            if(j >= 4){
                a -= j - 3;
            }else if(j > 0){
                a += j;
            }

            a = (elimination.length + a) % elimination.length;

            Node data = new Node(x, null);
            if(elimination[a].compareAndSet(null, data)){
                for (int k = 0; k < 1000; k++) {
//                    if(elimination[a].compareAndSet(done, null)){
//                        return;
//                    }
                }

                if(!elimination[a].compareAndSet(data, null)){
                    elimination[a].compareAndSet(done, null);
                    return;
                }

                break;
            }
        }

        while (true) {
            Node h = head.getValue();
            Node newHead = new Node(x, h);
            if (head.compareAndSet(h, newHead)) {
                return;
            }
        }
    }

    @Override
    public int pop() {
        int i = rnd.nextInt(elimination.length);
        for (int j = 0; j <= 6; j++) {
            int a = i;
            if(j >= 4){
                a -= j - 3;
            }else if(j > 0){
                a += j;
            }

            a = (elimination.length + a) % elimination.length;

            Node data = elimination[a].getValue();
            if(data != null && data != done && elimination[a].compareAndSet(data, done)){
                return data.x;
            }
        }

        while (true) {
            Node h = head.getValue();
            if (h == null) {
                return Integer.MIN_VALUE;
            }

            if (head.compareAndSet(h, h.next)) {
                return h.x;
            }
        }
    }
}
