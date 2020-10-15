package com.example.juc.cas;

/**
 *  CAS->Unsafe类->CAS思想->ABA->原子引用->如何规避ABA问题  连环炮
 *  ABA问题:
 *      两个线程A,B同时读取了某个值value1,线程A里面将将那个值改为value2,最后又改成了value1,当B线程更新的时候,发现值还是原来的value1,所以就去更新了.
 *      但是整个过程中A的值是发生了改变的,这就是ABA问题.(上面也有可能是线程C去将value2改为了value1)
 *  小口诀:
 *      ABA: 狸猫换太子
 *      多线程: 线程操作资源类
 *      Lambda表达式: 拷贝小括号,写死右箭头,落地大括号
 */
public class CASABADemo {
}
