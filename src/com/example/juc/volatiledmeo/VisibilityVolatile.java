package com.example.juc.volatiledmeo;


import java.util.concurrent.TimeUnit;

/**
 *  volatile关键字是java提供的一种轻量级的同步机制,可以理解为乞丐版的synchronized.
 *  volatile关键字:
 *      1.保证可见性
 *          当某一个用户线程对某个共享变量的值进行了修改,那么其它共享就能马上读取到该值
 *      2.不保证原子性
 *          可以使用Atomic原子类来处理
 *      3.防止指令重排
 *          这个在DCL里面有应用
 *
 *  概念理解:
 *      主内存:
 *
 */
public class VisibilityVolatile {

    public static void main(String[] args) {
        solutionVisibilityByVolatile();
        //showVisibility();
    }

    /**
     *  使用volatile解决线程可见性问题
     */
    private static void solutionVisibilityByVolatile() {
        MyData2 myData = new MyData2();

        new Thread(()->{

            System.out.println(Thread.currentThread().getName() + "开始执行");

            // 休眠,模拟业务
            try { TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) { e.printStackTrace(); }

            myData.addTo60();

            System.out.println(Thread.currentThread().getName() + "执行完了");
        }, "AAA").start();


        // 如果 num等于0就一直空转
        while(myData.num == 0){

        }

        /**
         * 在AAA线程中对num的值进行了修改,因为使用了volatile解决了可见性的问题,所以main线程会感知到值的修改,然后就去主内存读取
         */
        System.out.println(Thread.currentThread().getName() + "执行完了");
    }

    /**
     * 展示线程可见性造成的问题
     */
    private static void showVisibility() {
        MyData myData = new MyData();

        new Thread(()->{

            System.out.println(Thread.currentThread().getName() + "开始执行");

            // 休眠,模拟业务
            try { TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) { e.printStackTrace(); }

            myData.addTo60();

            System.out.println(Thread.currentThread().getName() + "执行完了");
        }, "AAA").start();


        // 如果 num等于0就一直空转
        while(myData.num == 0){

        }

        /**
         * 在AAA线程中对num的值进行了修改,但是因为可见性的问题,main线程是不知道的,所以会在上面的while里面一直空转,后面的代码不会执行
         */
        System.out.println(Thread.currentThread().getName() + "执行完了");
    }

}

class MyData{

    int num = 0;

    public void addTo60(){
        num = 60;
    }
}

class MyData2{

    volatile int num = 0;

    public void addTo60(){
        num = 60;
    }
}
