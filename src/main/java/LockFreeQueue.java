import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> {
    private  AtomicReference<Node<T>> head, tail;
    private  AtomicInteger size;

    public LockFreeQueue(AtomicReference<Node<T>> head, AtomicReference<Node<T>> tail, AtomicInteger size) {
      head = new AtomicReference<>(null);
      tail = new AtomicReference<>(null);
      size.set(0);
    }

    private class Node<T>{
        private volatile T value;

        private volatile Node<T> next;
        private volatile Node<T> previous;
        public Node(T value) {
            this.value = value;
            this.next = null;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<T> previous) {
            this.previous = previous;
        }
    }

    public void add(T elem){
        if(elem == null) {
            throw new NullPointerException();
        }
        Node<T> node = new Node<>(elem);
        Node<T> currentTail;
        do{
            currentTail = tail.get();
            node.setPrevious(currentTail);
        }while (!tail.compareAndSet(currentTail,node));

        if (node.previous != null){
            node.previous.next = node;
        }

        head.compareAndSet(null,node);
        size.incrementAndGet();
    }
    
}




