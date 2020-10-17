package com.example.juc.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 *  线程安全的容器
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {

        /**
         * 通过
         *    Collections.synchronizedList(List list)
         *    CopyOnWriteArrayList
         *    CopyOnWriteArraySet
         *    ConcurrentHashMap
         *  都可以获取线程安全的容器
         */
        // List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0, 8));
            },String.valueOf(i)).start();
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list);
    }
}
