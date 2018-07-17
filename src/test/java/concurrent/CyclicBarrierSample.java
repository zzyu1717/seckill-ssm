package concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierSample {

    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(5, ()->System.out.println("Action...GO again"));

        for (int i = 0; i < 5; i++) {

            Thread t = new Thread(new CyclicWorker(barrier));
            t.start();

        }
    }

}

class CyclicWorker implements Runnable {

    private CyclicBarrier barrier;

    public CyclicWorker(CyclicBarrier barrier) {
        this.barrier = barrier;
    }


    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {

                System.out.println("Executed!");

                barrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
