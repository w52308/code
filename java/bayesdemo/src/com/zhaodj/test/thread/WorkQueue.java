package com.zhaodj.test.thread;

import java.util.LinkedList;

public class WorkQueue {
	
	private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList<Runnable> queue;
    
    public WorkQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList<Runnable>();
        threads = new PoolWorker[this.nThreads];
        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }
    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
    }
    
    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;
            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }
                    r = queue.removeFirst();
                }
                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
    
    public static void main(String[] args){
    	WorkQueue wq=new WorkQueue(10);
    	
    }

}
