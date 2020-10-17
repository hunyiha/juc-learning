package com.example.juc.tools;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  等待多线程完成的并发工具类:CountDownLatch
 */
public class CountDownLatchDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            try {
                new Thread(()->{
                    try {
                        for (int j = 0; j < 100; j++) {
                            atomicInteger.getAndIncrement();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                        // 保证每个线程最后都能进行 -1
                        countDownLatch.countDown();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
                // 避免线程创建失败
                countDownLatch.countDown();
            }
        }

        try {
            // 等待多线程计算完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(atomicInteger.get());
    }


}
