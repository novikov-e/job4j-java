package multithreading.wait;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import java.util.LinkedList;
import java.util.Queue;
/**
 * Class SimpleBlockingQueue.
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1$
 * @since 0.1
 */
@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int limit;

    public SimpleBlockingQueue(int limit) {
        this.limit = limit;
    }

    public void offer(T value) throws InterruptedException {
        synchronized (this.queue) {
            queue.offer(value);
            if (queue.size() == limit) {
                queue.wait();
            }
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this.queue) {
            if (queue.isEmpty()) {
                queue.notify();
            }
            return queue.poll();
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
