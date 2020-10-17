package com.example.juc.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *  循环屏障CyclicBarrier
 *
 *  CyclicBarrier的字面意思是可循环使用（Cyclic）的屏障（Barrier）。
 *
 *  它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续运行。
 *
 *  CyclicBarrier有点类似于人齐了,才开会
 *
 *  而且CyclicBarrier是可循环的,可以reset重置
 **/
public class CyclicBarrierDemo {

    public static void main(String[] args) {

        // 当达到屏障2才会执行 runable 即打印
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, ()->{
            System.out.println("人齐了,开会");
        });

        new Thread(()->{
            System.out.println(1);
            System.out.println(2);
            try {
                // 达到了之后就等待
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "AAA").start();

        new Thread(()->{
            System.out.println(3);
            System.out.println(4);
            try {
                // 达到了之后就等待
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "BBB").start();
    }
}
