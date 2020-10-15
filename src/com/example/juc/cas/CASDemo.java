package com.example.juc.cas;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  CAS: compare and set,这就是一种思想
 *
 *  为什么Atomic原子类能保证原子性和同步问题:
 *     1.使用了CAS思想,并没有通过加锁(CAS自旋)
 *     2.AtomicInteger中的value是volatile修饰的,保证了可见性
 *     3.使用Unsafe这个类,这个类里面都是native方法,简单的说就是底层是通过CPU原语保证了原子性
 *
 *   AtomicInteger的缺点:
 *     1.只保证一个共享变量的原子性
 *     2.循环时间长,开销很大
 *     3.ABA问题
 *
 *
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        while (!atomicInteger.compareAndSet(2,3)){
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicInteger.set(2);
        }
        System.out.println(atomicInteger.get());

        //  jre/lib/rt.jar/sun/misc下的Unsafe类
        atomicInteger.getAndIncrement();
    }
}
