package concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * sempahore 非典型用法
 *
 */

public class AbnormalSemaphoreSample {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new MyWorker(semaphore));
            t.start();
        }

        System.out.println("Action...GO!");

        semaphore.release(5);

        System.out.println("Wait for permits off");

        while (semaphore.availablePermits() != 0) {
            TimeUnit.MILLISECONDS.sleep(100L);
        }

        System.out.println("Action...GO again!");

        semaphore.release(5);
    }
}

class MyWorker implements Runnable {
    private String name;
    private final Semaphore semaphore;

    public MyWorker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {

        try {
            semaphore.acquire();
            log("executed!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(String msg) {
        if (name == null) {
            name = Thread.currentThread().getName();
        }
        System.out.println(name + " " + msg);
    }
}
