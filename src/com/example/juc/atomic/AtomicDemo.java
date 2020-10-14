package com.example.juc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: juc-learning->AtomicDemo
 * @description: AtomicInteger原子类解决并发问题
 * @author: hunyiha
 * @create: 2020-10-14 21:52
 **/
public class AtomicDemo {
    public static void main(String[] args) throws InterruptedException {

/*        AddNumber1 addNumber1 = new AddNumber1(0);

        new Thread(()->{
            addNumber1.addOne();
        }).start();

        new Thread(()->{
            addNumber1.addOne();
        }).start();

        TimeUnit.SECONDS.sleep(5);

        System.out.println(addNumber1.getNum());*/

        AddNumber2 addNumber2 = new AddNumber2();

        new Thread(()->{addNumber2.addOne();}).start();
        new Thread(()->{addNumber2.addOne();}).start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(addNumber2.getNum().get());

    }
}

class AddNumber1{

    private int num;

    public AddNumber1(int num) {
        this.num = num;
    }

    public void addOne(){
        for (int i = 0; i < 100000; i++) {
            num++;
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

class AddNumber2{

   private AtomicInteger num = new AtomicInteger(0);

    public void addOne(){
        for (int i = 0; i < 100000; i++) {
            num.getAndIncrement();
        }
    }

    public AtomicInteger getNum() {
        return num;
    }

    public void setNum(AtomicInteger num) {
        this.num = num;
    }
}
