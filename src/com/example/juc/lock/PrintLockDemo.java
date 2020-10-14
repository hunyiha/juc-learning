package com.example.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: juc-learning->PrintLockDemo
 * @description:  三个线程分别打印A,B,C  打印AAABBBBCCCCCC 共打印10轮
 * @author: hunyiha
 * @create: 2020-10-14 21:02
 **/
public class PrintLockDemo {
    public static void main(String[] args) {

        PrintTask pt = new PrintTask();

        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                pt.printA(i);
            }
        }, "A").start();

        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                pt.printB(i);
            }
        }, "B").start();

        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                pt.printC(i);
            }
        }, "C").start();

    }
}

class PrintTask {

    private  int number = 1; //当前正在执行的线程的标记

    private Lock lock = new ReentrantLock();

    private  Condition conditionA = lock.newCondition();
    private  Condition conditionB = lock.newCondition();
    private  Condition conditionC = lock.newCondition();

    public void printA(int totalLoop){
        lock.lock();

        try{
            // 1.判断
            if( number != 1){
                conditionA.await();
            }

            // 2.打印
            for (int i = 1; i <= 3; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + totalLoop + "\t" + i);
            }

            // 3.唤醒
            number = 2;
            conditionB.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void printB(int totalLoop){
        lock.lock();

        try{
            if( number != 2){
                conditionB.await();
            }

            for (int i = 1; i <= 4; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + totalLoop + "\t" + i);
            }

            number = 3;
            conditionC.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void printC(int totalLoop){
        lock.lock();

        try{
            if( number != 3){
                conditionC.await();
            }

            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + totalLoop + "\t" + i);
            }

            number = 1;
            conditionA.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


}
