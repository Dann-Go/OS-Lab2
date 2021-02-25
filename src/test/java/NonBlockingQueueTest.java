import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

class NonBlockingQueueTest {

    @Test
    public void  pushPopEmptyTest() throws InterruptedException {
        NonBlockingQueue<Integer> queue = new NonBlockingQueue<>();

        Thread[] threads = new Thread[20];
        for (int i = 0 ; i < 10; i++) {
            final int index = i + 1;
            threads[i] = new Thread(() -> {
                queue.push(2 * index - 1);
                queue.push(2 * index);
            });
        }
        for (int i = 10; i < 20; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0 ; j < 2; j++) {
                    queue.pop();
                }
            });
        }
        IntStream.range(0, 20).forEach(i -> threads[i].start());
        for(Thread t : threads) {
            t.join();
        }
        assertTrue(queue.empty());
    }

    @Test
    public void containsTest() throws InterruptedException {
        NonBlockingQueue<Integer> queue = new NonBlockingQueue<>();
        Thread thread1 = new Thread(() -> queue.push(10));
        Thread thread2 = new Thread(() -> queue.push(20));

        thread1.start();
        thread2.start();

        thread1.join();
        boolean check = queue.contains(10);
        thread2.join();

        assertTrue(check);
    }

    @Test
    public void nullPopTest() {
        NonBlockingQueue<String> queue = new NonBlockingQueue<>();
        assertNull(queue.pop());
    }

    @Test
    public void peekTest() {
        NonBlockingQueue<String> queue = new NonBlockingQueue<>();
        queue.push("Simon");
        assertEquals("Simon", queue.peek());
    }
}