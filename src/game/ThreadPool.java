package game;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {


    ReentrantLock lock = new ReentrantLock();

    Condition isEmpty = new ReentrantLock().newCondition();

    int numThreads;
    LinkedBlockingQueue<Runnable> list = new LinkedBlockingQueue<Runnable>();

    ClassTrabalhadora[] trabalhadores;

    public ThreadPool(int numThreads){
        this.numThreads = numThreads;
        execute();
    }

    public void submit(Runnable tarefa){
        lock.lock();
        list.add(tarefa);
        isEmpty.signalAll();
        lock.unlock();
    }

    public void execute(){
        trabalhadores = new ClassTrabalhadora[numThreads];
        for(int i = 0;i<numThreads;i++){
            trabalhadores[i] = new ClassTrabalhadora() ;
            trabalhadores[i].start();
        }
    }



    public class ClassTrabalhadora extends Thread{


        @Override
        public void run() {

            while(true) {
                lock.lock();
                while (list.isEmpty()) {
                    try {
                        isEmpty.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                Runnable tarefa = list.poll();

                tarefa.run();

                lock.unlock();
            }
        }
    }


}
