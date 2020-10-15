package com.example.juc.singleton;

/**
 * @program: juc-learning->SingletonDemo
 * @description: 双端检锁DCL下的单列模式
 * @author: hunyiha
 * @create: 2020-10-15 21:07
 **/
public class SingletonDemo {

    /**
     * 懒汉式是在获取的时候才初始化,恶汉是在初始化的时候就赋值
     *
     * volatile防止指令重排,因为singletonDemo = new SingletonDemo();是分为三个步骤:
     *      1.分配内存空间
     *      2.初始化
     *      3.将引用指向内存空间
     *  因为2和3并没有数据依赖关系,就可能发生指令重排,当发生指令重排之后,123可能变成了132
     *      分配内存空间
     *      引用指向内存空间(完成之后,singletonDemo就不为null了,如果在下面的初始化操作还没执行前再来一个线程获取,那么返回的就是一个没有初始化的变量)
     *      初始化操作
     *   总结:
     *      instance不为null时,只是说明instaance已经指向了内存空间,但是并不一定完成了初始化操作,这样就可能造成线程安全问题
     */

    private static volatile SingletonDemo singletonDemo;

    // 单列模式的灵魂,构造方法私有
    private SingletonDemo() {
        System.out.println("我的构造方法被调用了");
    }

    /**
     *
     * 如果将方法上加synchronized,这样是可以解决的,但是太重了.
     * 现在企业中流行的解决是使用DCL方式(double check lock)双端检锁机制
     *      在加锁前后都判断一下是否为null
     *
     */
    public static SingletonDemo getInstance(){
        if(singletonDemo == null){
            synchronized (SingletonDemo.class){
                if(singletonDemo == null){
                    singletonDemo = new SingletonDemo();
                }
            }
        }
        return singletonDemo;
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            new Thread(()->{getInstance();}).start();
        }

    }
}

