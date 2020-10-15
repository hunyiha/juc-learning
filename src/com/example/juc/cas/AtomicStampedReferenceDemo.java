package com.example.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference带有版本号的原子引用,类似于乐观锁 通过版本解决ABA问题
 * 如果只是进行值比较是没有办法避免ABA问题的
 *
 * 希望你以后事少钱多离家近,别人加班你加薪
 */
public class AtomicStampedReferenceDemo {

    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);
    public static void main(String[] args) {
        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println("AAA第一次的版本号: " + stamp);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedReference.compareAndSet(100, 101, stamp, stamp + 1);

            stamp = atomicStampedReference.getStamp();
            System.out.println("AAA第二次的版本号: " + stamp);
            atomicStampedReference.compareAndSet(101, 100, stamp, stamp + 1);
        }, "AAA").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println("BBB线程第一次获取的版本号: " + stamp);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicStampedReference.compareAndSet(100, 2020, stamp, stamp + 1));

            System.out.println(Thread.currentThread().getName() + "\t" + atomicStampedReference.getReference());
        }, "BBB").start();
    }
}
