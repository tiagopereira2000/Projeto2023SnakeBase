package game;

import java.time.Clock;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {


    ReentrantLock lock = new ReentrantLock();

    Condition isEmpty = lock.newCondition();

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


    public void shutdownNow(){
        for(ClassTrabalhadora t:trabalhadores){
            t.interrupt();
        }
    }



    public class ClassTrabalhadora extends Thread{


        @Override
        public void run() {

            while(true) {

                try {
                lock.lock();
                System.out.println("Running obstacle mover...");
                while (list.isEmpty()) {

                        isEmpty.await();


                }

                Runnable tarefa = list.poll();

                tarefa.run();



                }

                catch (InterruptedException e) {
                    System.out.println("Obstaculo Interrompido");
                }

                finally {
                    lock.unlock();

                }


            }
        }
    }


}
