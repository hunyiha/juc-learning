package com.example.juc.volatiledmeo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile不保证原子性,但是可以使用Atomic原子类来解决这个问题
 */
public class AtomicVolatile {

    public static void main(String[] args) {
        showAtomicVolatile();
        showAtomicInteger();
    }

    private static void showAtomicInteger() {
        NumAdd1 numAdd = new NumAdd1();

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    numAdd.addOne();
                }
            }, String.valueOf(i)).start();
        }

        while(Thread.activeCount() >2){
            /**
             * Thread.yield()的作用:
             *    使当前线程从执行状态（运行状态）变为可执行态（就绪状态）。cpu会从众多的可执行态里选择，
             *         也就是说，当前也就是刚刚的那个线程还是有可能会被再次执行到的，并不是说一定会执行其他线程而该线程在下一次中不会执行到了。
             *
             *    Java线程中有一个Thread.yield( )方法，很多人翻译成线程让步。
             *         顾名思义，就是说当一个线程使用了这个方法之后，它就会把自己CPU执行的时间让掉，让自己或者其它的线程运行。
             */
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "num的值:" + numAdd.atomicInteger.get());
    }

    /**
     * 此时的num是被volatile修饰的,但是还是没有保证原子性.
     */
    private static void showAtomicVolatile() {
        NumAdd numAdd = new NumAdd();

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    numAdd.addOne();
                }
            }, String.valueOf(i)).start();
        }

        while(Thread.activeCount() >2){
            /**
             * Thread.yield()的作用:
             *    使当前线程从执行状态（运行状态）变为可执行态（就绪状态）。cpu会从众多的可执行态里选择，
             *         也就是说，当前也就是刚刚的那个线程还是有可能会被再次执行到的，并不是说一定会执行其他线程而该线程在下一次中不会执行到了。
             *
             *    Java线程中有一个Thread.yield( )方法，很多人翻译成线程让步。
             *         顾名思义，就是说当一个线程使用了这个方法之后，它就会把自己CPU执行的时间让掉，让自己或者其它的线程运行。
             */
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "num的值:" + numAdd.num);
    }
}

class NumAdd{
    volatile int num = 0;

    public void addOne(){
        num++;
    }
}

class NumAdd1{
    AtomicInteger atomicInteger = new AtomicInteger();

    public void addOne(){
        atomicInteger.getAndIncrement();
    }
}

