
// Volatile keyword is used to avoid memory consistency errors in multithreaded programs. It tells the compiler to
// avoid doing any optimizations to the variable. If you mark a variable as volatile, the compiler won’t optimize or
// reorder instructions around that variable.
//
//Also, The variable’s value will always be read from the main memory instead of temporary registers.

public class VolatileKeywordExample {

    //volatile variable
    private static volatile boolean sayHello = false;

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