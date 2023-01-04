package multithreading.synchronization.storage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
/**
 * Class User.
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1
 * @since 0.1
 */
@ThreadSafe
public class User {
    @GuardedBy("this")
    private final int id;
    @GuardedBy("this")
    private final int amount;

    public User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized int getAmount() {
        return amount;
    }
}
