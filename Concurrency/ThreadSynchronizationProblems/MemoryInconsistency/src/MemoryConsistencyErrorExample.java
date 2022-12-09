
/*
 Memory inconsistency errors occur when different threads have inconsistent views of the same data.
 This happens when one thread updates some shared data, but this update is not propagated to other threads,
  and they end up using the old data.
 Why does this happen? Well, there can be many reasons for this. The compiler does several optimizations to
  your program to improve performance. It might also reorder instructions in order to optimize performance.
  Processors also try to optimize things, for instance, a processor might read the current value of a variable from
  a temporary register (which contains the last read value of the variable), instead of main memory (which has the
  latest value of the variable).
 */


// Sample of Memory Inconsistency
public class MemoryConsistencyErrorExample {
    private static boolean sayHello = false;

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            while(!sayHello) {
            }

            System.out.println("Hello World!");

            while(sayHello) {
            }

            System.out.println("Good Bye!");
        });

        thread.start();

        Thread.sleep(1000);
        System.out.println("Say Hello..");
        sayHello = true;

        Thread.sleep(1000);
        System.out.println("Say Bye..");
        sayHello = false;
    }
}

//The program doesn’t even terminate.
// The first thread is unaware of the changes done by the main thread to the sayHello variable.
//You can use volatile keyword to avoid memory consistency errors.