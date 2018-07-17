package concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LatchSample {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(6);

        for (int i = 0; i < 5; i++) {

            Thread t = new Thread(new FirstBatchWorker(latch));
            t.start();

        }

        for (int i = 0; i < 5; i++) {

            Thread t = new Thread(new secondBatchWorker(latch));
            t.start();

        }

        /**
         * 演示目的逻辑，不推荐
         */
        while (latch.getCount() != 1) {
            TimeUnit.MILLISECONDS.sleep(100L);
        }

        System.out.println("Wait for first batch finish");

        latch.countDown();
    }
}

class FirstBatchWorker implements Runnable{
    private CountDownLatch latch;

    public FirstBatchWorker(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void run() {
        System.out.println("First batch executed!");
        latch.countDown();
    }


}


class secondBatchWorker implements Runnable{
    private CountDownLatch latch;

    public secondBatchWorker(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Second batch executed!");
    }


}