package com.example.juc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;


/**
 *  分支合并框架ForkJoin
 *
 *  ForkJoinPool  池子
 *  ForkJoinTask  抽象类,实现了Future接口
 *  RecursiveTask 继承了ForkJoinTask
 *
 *  计算: 0 ... 100的和
 */
public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        MyTask myTask = new MyTask(0, 100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);

        System.out.println("计算结果是:" + forkJoinTask.get());
        // 关闭池子
        forkJoinPool.shutdown();
    }
}

class MyTask extends RecursiveTask<Integer> {

    // 临界值10
    public static final Integer ADJUST_VALUE = 10;
    private int begin;
    private int end;
    private int result;

    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        // 小于临界值直接进行计算, 不在继续分
        if(end-begin <= ADJUST_VALUE){
            for (int i = begin; i <= end; i++) {
                result  = result + i;
            }
        }else {
            int middle = (end +begin) / 2;
            MyTask myTask1 = new MyTask(begin, middle);
            MyTask myTask2 = new MyTask(middle+1, end);

            // 调用fork()还是去调用compute()
            myTask1.fork();
            myTask2.fork();

            result = myTask1.join() + myTask2.join();
        }
        return result;
    }
}
