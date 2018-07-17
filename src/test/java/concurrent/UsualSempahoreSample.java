package concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class UsualSempahoreSample {
    public static void main(String[] args) {
        System.out.println("Action...GO!");
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new SemaphoreWorker(semaphore));
            t.start();
        }
    }
}

class SemaphoreWorker implements Runnable {
    private String name;
    private final Semaphore semaphore;

    public SemaphoreWorker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {

        try {
            log("is waiting for a permit!");
            semaphore.acquire();
            log("acquired a permit");
            log("executed!");
            //TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log("released a permit!");
            semaphore.release();
        }
    }

    public void log(String msg) {
        if (name == null) {
            name = Thread.currentThread().getName();
        }
        System.out.println(name + " " + msg);
    }
}
