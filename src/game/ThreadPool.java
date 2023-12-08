package game;

import java.util.concurrent.LinkedBlockingQueue;


public class ThreadPool {

    private final Worker[] workers;
    private final LinkedBlockingQueue<ObstacleMover> list = new LinkedBlockingQueue<>();   //list of tasks (ObstacleMover)

    public ThreadPool(int numThreads){
        workers = new Worker[numThreads];
        for(int i=0; i < numThreads; i++)
            workers[i] = new Worker();
    }

    public void submit(ObstacleMover tarefa) {
        try{
            list.put(tarefa);
        }catch (InterruptedException e){
            System.out.println("Interrupted Submit");
        }
    }

    public void execute(){
        for(Worker w: workers)
            w.start();
    }


    public void shutdownNow(){
        for(Worker t: workers){
            t.interrupt();
        }
    }



    class Worker extends Thread{

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {}
            while(true) {
                try {
                    ObstacleMover mover = list.take();
                    mover.start();
                    mover.join();
                } catch (InterruptedException e) {
                    System.out.println("Worker Interrupted");
                    break; //end worker
                }

            }
        }
    }


}
