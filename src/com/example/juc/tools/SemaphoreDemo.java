package com.example.juc.tools;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore 可以控制并发数,  多个线程,抢夺多个资源
 *
 * Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源。
 *
 * 类似于: 小区抢停车位  抢红包
 *
 * acquire:     获得   əˈkwī(ə)r
 * release:     释放   rəˈlēs
 * semaphore:   信号   ˈseməˌfôr
 *
 *
 * reference: https://juejin.im/post/6844904054485680141#heading-6
 *
 * 应用场景:
 *
 *     Semaphore可以用于做流量控制，特别是公用资源有限的应用场景，比如数据库连接。
 *     假如有一个需求，要读取几万个文件的数据，因为都是IO密集型任务，我们可以启动几十个线程并发地读取，但是如果读到内存后，
 *        还需要存储到数据库中，而数据库的连接数只有10个，这时我们必须控制只有10个线程同时获取数据库连接保存数据，否则会报错无法获取数据库连接。
 *

 */
public class SemaphoreDemo {
    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(10);

        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "抢到了车位");

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "离开了车位");
                semaphore.release();
            }, String.valueOf(i)).start();
        }
    }
}
