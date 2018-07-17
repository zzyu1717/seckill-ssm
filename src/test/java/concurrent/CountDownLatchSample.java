package concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchSample {
    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.main();
    }
}

class Driver {
    public void main() {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

            /**
             * 工作之前的准备，线程不能运行
             */
            prepareForDoWork();
            /**
             * 所有线程可以执行
             */
            startSignal.countDown();

            /**
             * 收尾工作
             *
             */
             AfterOfDoWork();

            /**
             * 等待所有线程结束
             */
            try {
                doneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


    }


    public void prepareForDoWork() {
        System.out.println(Thread.currentThread().getName() + " before do work()...");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void AfterOfDoWork() {
        System.out.println(Thread.currentThread().getName() + " after do work()...");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {

        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doWork() {
        System.out.println(Thread.currentThread().getName() + " start do work()...");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end do work()...");

    }

}