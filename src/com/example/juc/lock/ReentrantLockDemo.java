package com.example.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: juc-learning->ReentrantLockDemo
 * @description:
 * @author: hunyiha
 * @create: 2020-10-14 20:51
 **/
public class ReentrantLockDemo {
    public static void main(String[] args) {

        Thread thread1 = new Thread(new Task(1));
        Thread thread2 = new Thread(new Task(2));

        thread1.start();
        thread2.start();

    }
}

class Task implements Runnable{

    private static ReentrantLock lock = new ReentrantLock();

    private int idc;

    public Task(int idc) {
        this.idc = idc;
    }

    @Override
    public void run() {

        // 加锁
        lock.lock();

        System.out.println(Thread.currentThread().getName() + "-----" + idc);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 在finally块中,保证锁能够释放
            lock.unlock();
        }
    }
}
