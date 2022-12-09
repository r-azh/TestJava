import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
Thread interference and memory consistency errors can be avoided by ensuring the following two things-

Only one thread can read and write a shared variable at a time. When one thread is accessing a shared variable,
 other threads should wait until the first thread is done. This guarantees that the access to a shared variable is
 Atomic, and multiple threads do not interfere.

Whenever any thread modifies a shared variable, it automatically establishes a happens-before relationship with
subsequent reads and writes of the shared variable by other threads. This guarantees that changes done by one thread
are visible to others.

Java has a synchronized for synchronize access to any shared resource, thereby avoiding both kinds of errors.
Note that the concept of Synchronization is always bound to an object.
But threads can safely call increment() method on different instances of SynchronizedCounter at the same time, and
that will not result in a race condition.

In case of static methods, synchronization is associated with the Class object.

Java internally uses a so-called intrinsic lock or monitor lock to manage thread synchronization. Every object has an
intrinsic lock associated with it.

When a thread calls a synchronized method on an object, it automatically acquires the intrinsic lock for that object
and releases it when the method exits. The lock release occurs even if the method throws an exception.
 */


class SynchronizedCounter {
    private int count = 0;

    // Synchronized Method
    // The synchronized keyword makes sure that only one thread can enter the increment() method at one time.
    public synchronized void increment() {
        count = count + 1;
    }

    // synchronized keyword can also be used as a block statement, but unlike
    // synchronized method, synchronized statements must specify the object that
    // provides the intrinsic lock
    public void increment2() {
        // Synchronized Block -

        // Acquire Lock
        synchronized (this) {
            count = count + 1;
        }
        // Release Lock
    }

    public int getCount() {
        return count;
    }
}


public class SynchronizedMethodExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();

        for(int i = 0; i < 1000; i++) {
            executorService.submit(() -> synchronizedCounter.increment());
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println("Final count is : " + synchronizedCounter.getCount());
    }
}