import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingQueue<T> {

    private  AtomicReference<Node<T>> head, tail;
    private  AtomicInteger size;

    public NonBlockingQueue() {
      head = new AtomicReference<>(null);
      tail = new AtomicReference<>(null);
      size = new AtomicInteger();
      size.set(0);
    }

    private class Node<T> {

        private volatile T value;
        private volatile Node<T> next;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }
    }

    public void push(T elem) {
        if(elem == null) {
            throw new NullPointerException();
        }
        Node<T> node = new Node<>(elem);
        Node<T> currentTail;
        do {
            currentTail = tail.get();
        } while (!tail.compareAndSet(currentTail, node));
        if(currentTail != null)
            currentTail.next = node;

        head.compareAndSet(null,node);
        size.incrementAndGet();
    }

    public T pop() {
        if(head.get() == null) {
            return null;
        }
        Node<T> currentHead;
        Node<T> nextNode;
        do {
            currentHead = head.get();
            nextNode =  currentHead.next;
        } while (!head.compareAndSet(currentHead, nextNode));
        size.decrementAndGet();
        return currentHead.value;
    }

    public boolean contains(T elem) {
        if (elem == null )
            return false;
        for (Node<T> n = head.get(); n != null; n = n.next) {
            if (n.value.equals(elem))
                return true;
        }
        return false;
    }

    public T peek() {
        if(head.get() == null) {
           return null;
        }
        return head.get().value;
    }

    public boolean empty() {
        return size.get() == 0;
    }
}




