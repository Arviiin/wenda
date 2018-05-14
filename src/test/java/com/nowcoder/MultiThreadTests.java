package com.nowcoder;

import java.awt.font.TextHitInfo;
import java.util.concurrent.*;//concurrent  java并发包
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid){
        this.tid = tid;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("%d:%d" ,tid,i));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

/**
 * BlockingQueue同步队列
 */
class Consumer implements Runnable {
    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(1000);
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MultiThreadTests{

    public static void testThread(){

        for (int i = 0; i < 10; i++) {
            //new MyThread(i).start();
        }

        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10; j++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 %d: %d",finalI,j));

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    private static Object obj = new Object();
    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 10; j++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3 %d：", j));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (/*obj*/new Object()) {
            try {
                for (int j = 0; j < 10; j++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 %d：", j));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static void testSynchronized() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }



    public static void testBlockingQueue(){
        BlockingQueue<String> q = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();

    }

    /**
     * ThreadLocal
     */
    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal(){
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadLocalUserIds.set(finalI);
                        Thread.sleep(1000);
                        System.out.println("ThreadLocal:" + threadLocalUserIds.get());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userId = finalI;
                        Thread.sleep(1000);
                        System.out.println("UserId:" + userId);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    /**
     * 线程池
     */
    public static void testExecutor(){
        //ExecutorService service = Executors.newSingleThreadExecutor();单线程情况
        ExecutorService service = Executors.newFixedThreadPool(2);//2线程情况

        service.submit(new Runnable() {//提交任务
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });


        service.submit(new Runnable() {//提交任务
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
                System.out.println("wait for termination");

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger();//线程安全

    public static void testWithoutAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            counter++;
                            System.out.println(counter);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testWithAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testAtomic(){
//        testWithoutAtomic();
        testWithAtomic();
    }

    /**
     * Future
     * 1.返回异步结果
     * 2.阻塞等待返回结果
     * 3.timeout
     * 4.获取线程中的Exception
     */
    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {

                Thread.sleep(1000);
//                throw new IllegalArgumentException("异常");
                return 1;
            }
        });

        service.shutdown();
        try {
//            System.out.println(future.get());
            System.out.println(future.get(100, TimeUnit.MICROSECONDS));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        //testThread();
        //testSynchronized();
        //testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testAtomic();
        testFuture();
    }
}
