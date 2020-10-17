package com.example.juc.tools;

import java.util.Arrays;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Exchanger（交换者）是一个用于线程间协作的工具类。
 *
 *  Exchanger用于进行线程间的数据交换。它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。
 *    这两个线程通过exchange方法交换数据，如果第一个线程先执行exchange()方法，它会一直等待第二个线程也执行exchange()方法，
 *    当两个线程都到达同步点时，这两个线程就可以交换数据，将本线程生产出来的数据传递给对方。
 *
 *  Exchanger可以用于 遗传算法。 遗传算法 里需要选出两个人作为交配对象，这时候会交换两人的数据，并使用交叉规则得出2个交配结果。
 */
public class ExchangerDemo {
    private static final Exchanger<String> exgr = new Exchanger<String>();

    // 线上环境还是自己定义线程池,不要使用Executors工具类创建线程池
    private static ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // A录入银行流水数据
                    String A = "银行流水A";
                    /**
                     * 如果两个线程有一个没有执行exchange()方法，则会一直等待，如果担心有特殊情况发生，避免一直等待，可以使用
                     *     exchange(V x, long timeout, TimeUnit unit)设置最大等待时长。
                     */
                    exgr.exchange(A);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // B录入银行流水数据
                    String B = "银行流水B";
                    String A = exgr.exchange(B);
                    System.out.println("A和B数据是否一致：" + A.equals(B)
                            + ", A录入的是：" + A + ", B录入是：" + B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPool.shutdown();
    }
}
