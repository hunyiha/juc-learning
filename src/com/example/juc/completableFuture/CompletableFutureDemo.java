package com.example.juc.completableFuture;

import com.sun.media.jfxmediaimpl.platform.osx.OSXPlatform;
import jdk.management.resource.internal.FutureWrapper;

import java.util.concurrent.*;

/**
 *  CompletableFuture可以进行异步编排.
 *     CompletableFuture这是1.8添加的, 实现了Future这个接口.
 *     FutureTask实现了RunnableFuture接口,RunnableFuture又继承了Runnable, Future这两个接口.
 *     Future最大的特点就是可以获取异步执行的结果.
 *
 *
 *  异步任务A,B,C,如果C需要A的返回结果,B不需要这种复杂场景的异步任务,我们就需要使用异步任务编排工作.
 *      也就是说在义务复杂情况下,一个异步调用肯恩那个以来另一个异步调用的执行结果
 *
 *
 *  业务场景:
 *      1.获取sku的基本信息  0.5s
 *      2.获取sku的图片信息  0.5s
 *      3.获取sku的促销信息  1s
 *      4.获取spu的所有销售属性 1s
 *      5.获取规格参数组及组下的规格参数 1.5s
 *      6.spu信息 1s
 *
 *      假如上坪详情页的每个查询,需要如下的标注时间才能完成,那么每个用户需要6.5s后才能看到商品详情页的内容.很显然是不能接受的.
 *      如果有多个线程同时完成这6个步骤,业务只需要1.5s即可完成相映.
 *
 *      上面的1,2,3可以同时进行,4,5,6都依赖1查询出来的skuid.
 *
 *    CompletableFuture  Ctrl + F12
 *
 *
 * CompletableFuture提供了四个静态方法来创建一个异步操作:
 *    public static CompletableFuture<Void> runAsync(Runnable runnable);
 *    public static CompletableFuture<Void> runAsync(Runnable runnable,Executor executor);
 *    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier);
 *    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier,Executor executor);
 *
 *    1.runXXX都是没有返回结果的(泛型是Void),supplyAsync都是可以获取返回结果的
 *    2.可以传入自定义的线程池,否则就使用默认的线程池
 *    3.一般况下,我们都是使用2,4这两种,使用自己的线程池
 *
 * CompletableFuture提供的完成时回调:
 *    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action);
 *    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action);
 *    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor);
 *    public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn);
 *
 *        whenComplete可以设置成功回调什么?可以处理正常和异常的计算结果,exceptionally处理异常情况.
 *
 *        whenComplete和whenCompleteAsync的区别:
 *             whenComplete:是执行当前任务的线程执行继续执行whenComplete的任务.
 *             whenCompleteAsync:是执行把whenCompleteAsync这个任务继续提交给线程池.
 *        方法不以Async结尾,意味着Action(完成后做的事)使用相同的线程执行,而Async可能会使用其它线程执行(如果是使用相同的线程池,也可能会被同一个线程选中执行)
 *
 * handle()方法
 *    handle()方法和complete一样,可对结果做最后的处理(可处理异常),可改变返回值.和whenComplete().exceptionally()差不多.
 *
 *
 * 线程串行化方式
 *      将异步任务A和B串联起来,比如B任务需要等到A任务执行完成,拿到A的结果才能处理.
 *
 *
 *
 *      thenRun 不需要上一个任务的执行结果
 *      thenAccept 需要上一个任务的执行结果,能感知上一个任务的执行结果
 *      thenApply 需要上一个任务的返回值,通过自己处理后再返回出去
 *
 * 两任务组合--都要完成:
 *     combine: 结合 联合 合并
 *     两个任务都必须完成,出发该任务.
 *
 *     thenCombine()组合两个future,获取两个future的返回结果,并返回当前任务的返回值.
 *     thenAcceptBoth()组合两个future,获取两个future任务的返回结果,然后处理任务,没有返回值
 *     runAfterBoth()组合两个future,不需要获取future的结果,只需要两个future处理完任务后,处理该任务
 *
 *
 *两任务组合--完成一个
 *     当两个任务中,任意一个future任务完成的时候,执行任务.
 *
 *     applyToEither: 两个任务有一个执行完成,获取它的返回值,处理任务并有新的返回值
 *     acceptEither: 两个任务有一个执行完成,获取它的返回值,没有新的返回值
 *     runAfterEithor:两个任务有一个执行完成,不需要future的结果,处理任务,也没有返回值
 *
 * 多任务组合:
 *    两个静态方法:
 *       public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
 *       public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
 *
 *    allOf:等待所有任务完成
 *    anyOf:只要有一个任务完成
 *
 */
public class CompletableFutureDemo {

    private static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * 使用CompletableFuture.runAsync(Runnable runnable,Executor executor)创建了一个异步任务,将异步任务交给线程池service去执行.
         * 程序不会中断的原因是后台创建了一个线程池.此时已经阻塞了.什么时候线程池销毁,程序也就停止了.
         */
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程: " + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果: " + i);
//        }, service);


        /**
         * 使用CompletableFuture.supplyAsync(Supplier<U> supplier,Executor executor)创建了一个异步任务,将异步任务交给线程池service去执行.有返回值.
         * 返回值是CompletableFuture类型的, CompletableFuture实现了Future接口,可以使用get()方法获取返回值.
         * 返回的是一个CompletableFuture对象,我们就可以使用它的编排方法.
         *
         * 如果想要当异步任务成功之后我们需做其它的事情,此时有两种方法:
         *      1.当它成功,我们使用代码来写什么什么
         *      2.使用CompletableFuture提供的异步编排功能,实现完成时回调
         *
         * whenComplete()中的参数是BiConsumer函数式接口,有两个参数,第一个是结果,第二个是异常.
         *      也就是说whenComplete()是可以感知异常的.虽然能够得到异常信息,但是没办法修改返回数据.
         * 如果拿到异常后需要详细处理,我们可以使用exceptionally(Function<Throwable, ? extends T> fn).
         *     Function函数式接口,里面的方法: R apply(T t);  接收T,返回R
         *     Function函数式接口在这里的作用就是接收一个exception,返回一个默认值.
         *     exceptionally()可以感知异常,同时返回默认值.
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程: " + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果: " + i);
//            return i;
//        }, service).whenComplete((result, exception) -> {
//            // 虽然能够感知异常信息,但是没有办法修改返回数据
//            System.out.println("异步任务成功完成了....结果是:" + result + ";异常是:" + exception);
//        }).exceptionally((e) -> {
//            //感知异常,同时返回默认值(也就是说修改了原来的返回值).
//            return 10;
//        });;

        /**
         * 方法执行完成后的处理,无论是成功还是异常
         *
         * handle()的参数:
         *      BiFunction<? super T, Throwable, ? extends U> fn
         *          R apply(T t, U u);
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程: " + Thread.currentThread().getId());
//            int i = 10 / 1;
//            System.out.println("运行结果: " + i);
//            return i;
//        }, service).handle((result, exception)->{
//            if(result != null){
//                return result;
//            }
//
//            if(exception != null){
//                return 0;
//            }
//            return 0;
//        });


        /**
         * 1. thenRun是没有返回值的,而且不能获取上一步的执行结果.
         *          .thenRunAsync(() -> {
         *             System.out.println("任务2启动了");
         *         }, service);
         * 2.thenAcceptAsyncn能接受上一步的结果,但是无返回值
         *          .thenAcceptAsync((result)->{
         *             System.out.println("任务2启动了," + result);
         *         }, service);
         * 3.thenApplyAsync能接受上一步的结果,同时也有返回值
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程: " + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果: " + i);
//            return i;
//        }, service).thenApplyAsync((result) -> {
//            System.out.println("任务2启动了" + result);
//            return 20;
//        }, service);


        /**
         * 两个都完成
         */
//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程开始: " + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("任务1线程结束");
//            return i;
//        }, service);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务2线程开始: " + Thread.currentThread().getId());
//            System.out.println("任务2线程结束");
//            return "Hello";
//        }, service);

        // 任务1和任务2执行完成之后执行,没有返回值,也不需要接收1和2的结果
//        CompletableFuture<Void> future = future01.runAfterBothAsync(future02, () -> {
//            System.out.println("任务3开始");
//        }, service);

        // 任务1和任务2执行完成之后执行,没有返回值,需要1和2的结果
//        future01.thenAcceptBothAsync(future02, (f1, f2) -> {
//            System.out.println("任务3开始, 任务1和任务2的结果: " + f1 + "\t" + f2);
//        }, service);

        // 任务1和任务2执行完成之后执行,接受1和2的返回值,同时自己也有返回值
//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
//            System.out.println("任务3开始, 任务1和任务2的结果: " + f1 + "\t" + f2);
//            return f1 + f2;
//        }, service);


        /**
         * 两个任务只要有一个完成之后就执行
         */

        //runAfterEithor:两个任务有一个执行完成,不需要future的结果,处理任务,也没有返回值
//        future01.runAfterEitherAsync(future02, ()->{
//            System.out.println("任务3执行完成了");
//        }, service);


        // acceptEither: 两个任务有一个执行完成,获取它的返回值,没有新的返回值
//        future01.acceptEitherAsync(future02, (f1)->{
//            System.out.println("任务3开始执行了,之前的返回值是:" + f1);
//        }, service);


        // applyToEither: 两个任务有一个执行完成,获取它的返回值,处理任务并有新的返回值
//        future01.applyToEitherAsync(future02, (f1)->{
//            System.out.println("任务3开始....之前的结果是:" + f1);
//            return 5;
//        }, service);


        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        }, service);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性");
            return "黑色+256G";
        }, service);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品介绍");
            return "华为";
        }, service);

        /**
         * 可以使用futureImg.get()  futureAttr.get()  futureDesc.get()这种方式,但是不优雅,虽然时间都是一样的
         * 我们推荐使用allOf()
         */
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);

        // 等待所有的任务执行完成, 也可以使用allof.join()
//        allOf.get();
        anyOf.get();
//        System.out.println("所有的查询已经完成, 查询结果:" + futureImg.get() + futureAttr.get() + futureDesc.get());
        System.out.println("所有的查询已经完成, 查询结果:" + anyOf.get());
    }
}
