
package com.unarin.cordova.beacon;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    private static int NUMBER_OF_CORES = 1; //Runtime.getRuntime().availableProcessors();

    //original
    //private static int KEEP_ALIVE_SECONDS = 30;

    private static int KEEP_ALIVE_SECONDS = 1;

    private boolean paused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();
    public PausableThreadPoolExecutor(BlockingQueue<Runnable> workQueue) {
        super(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, workQueue);
    }

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (paused) unpaused.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        pauseLock.lock();
        try {
            paused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            paused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}