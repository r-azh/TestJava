import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

// CompletableFuture executes these tasks in a thread obtained from the global ForkJoinPool.commonPool().
//
//But hey, you can also create a Thread Pool and pass it to runAsync() and supplyAsync() methods to let them execute
// their tasks in a thread obtained from your thread pool.


public class CompletableFutureExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        completableFutureSimple();

//        completableFutureRunAsync();

//        completableFutureRunAsyncLambda();

//        completableFutureSupplyAsync();

//        completableFutureSupplyAsyncLambda();

//        completableFutureSupplyAsyncWithThreadPool();

//        completableFutureThenApply();

//        completableFutureThenAccept();

//        completableFutureThenRun();

        completableFutureThenApplyAsync();
    }


    public static void completableFutureSimple() throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = new CompletableFuture<String>();
        try {
            String result = completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
        completableFuture.complete("Future's Result");
    }

    public static void completableFutureRunAsync() throws InterruptedException, ExecutionException {
        // Run a task specified by a Runnable Object asynchronously.
        // CompletableFuture.runAsync() is useful for tasks that don’t return anything.
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                // Simulate a long-running Job
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                System.out.println("I'll run in a separate thread than the main thread.");
            }
        });

        // Block and wait for the future to complete
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void completableFutureRunAsyncLambda() throws InterruptedException, ExecutionException {
        // Using Lambda Expression
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Simulate a long-running Job
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("I'll run in a separate thread than the main thread.");
        });

        // Block and wait for the future to complete
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void completableFutureSupplyAsync() {
        // if you want to return some result from your background task use CompletableFuture.supplyAsync()
        // Run a task specified by a Supplier object asynchronously
        // A Supplier<T> is a simple functional interface which represents a supplier of results. It has a single get()
        // method where you can write your background task and return the result.
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                return "Result of the asynchronous computation";
            }
        });

        // Block and get the result of the Future
        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void completableFutureSupplyAsyncLambda() {
        // method where you can write your background task and return the result.
        // Using Lambda Expression
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of the asynchronous computation";
        });

        // Block and get the result of the Future
        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void completableFutureSupplyAsyncWithThreadPool() {
        Executor executor = Executors.newFixedThreadPool(10);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of the asynchronous computation";
        }, executor);

        // Block and get the result of the Future
        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    // he CompletableFuture.get() method is blocking. It waits until the Future is completed and returns the result
    // after its completion.
    //
    //But, that’s not what we want right? For building asynchronous systems we should be able to attach a callback to
    // the CompletableFuture which should automatically get called when the Future completes.
    public static void completableFutureThenApply() {

        // Create a CompletableFuture
        CompletableFuture<String> whatsYourNameFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Rezvan";
        });

        // Attach a callback to the Future using thenApply()
        CompletableFuture<String> greetingFuture = whatsYourNameFuture.thenApply(name -> {
            return "Hello " + name;
        });

        CompletableFuture<String> welcomeText = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Rezvan";
        }).thenApply(name -> {
            return "Hello " + name;
        }).thenApply(greeting -> {
            return greeting + ", Welcome to the Java threading";
        });
        // In the above cases, the task inside thenApply() is executed in the same thread where the supplyAsync() task
        // is executed, or in the main thread if the supplyAsync() task completes immediately (try removing sleep()
        // call to verify).

        try {
            // Block and get the result of the future.
            System.out.println(greetingFuture.get()); // Hello Rezvan
            System.out.println(welcomeText.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void completableFutureThenAccept() {
        // If you don’t want to return anything from your callback function and just want to run some piece of code
        // after the completion of the Future, then you can use thenAccept() and thenRun() methods. These methods are
        // consumers and are often used as the last callback in the callback chain.

        // thenAccept() example
        CompletableFuture.supplyAsync(() -> {
            return "Rezvan";
        }).thenAccept(txt -> {
            System.out.println("Got name from thread " + txt);
        });
    }

    public static void completableFutureThenRun() {
        // While thenAccept() has access to the result of the CompletableFuture on which it is attached,
        // thenRun() doesn't even have access to the Future’s result.

        // thenRun() example
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Run some computation");
            return null;    // Why it sould have return??!!
        }).thenRun(() -> {
            System.out.println("Computation Finished.");
        });
    }

    // To have more control over the thread that executes the callback task, you can use async callbacks. If you use
    // thenApplyAsync() callback, then it will be executed in a different thread obtained from ForkJoinPool.commonPool()
    public static void completableFutureThenApplyAsync() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Run some computation.");
            return "Some Result";
        }).thenApplyAsync(result -> {
            // Executed in a different thread from ForkJoinPool.commonPool()
            System.out.println("Run some other computation in another thread.");
            return "Processed Result";
        });
        // If you pass an Executor to the thenApplyAsync() callback then the task will be executed in a thread obtained
        // from the Executor’s thread pool.
    }

}
