package com.example.juc.cas;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 *  JUC里面提供了AtomicInteger等,但是我要使用原子用户,原子订单等等原子类怎么办呢?
 *      可以使用AtomicReference<T>原子引用解决这个问题,T是泛型.
 *      如果想对某个类进行原子包装,可以使用AtomicReference这个类.
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {

        User z3 = new User("z3", 23);
        User l4 = new User("l4", 27);

        AtomicReference<User> atomicReference = new AtomicReference<>(z3);

        System.out.println(atomicReference.compareAndSet(z3, l4) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3, l4) + "\t" + atomicReference.get().toString());

    }
}


class User{
    private String username;
    private int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
