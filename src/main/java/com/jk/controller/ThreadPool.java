/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ThreadPool
 * Author:   李辉
 * Date:     2020/3/16 23:37
 * Description: aa
 * History:
 * <author>          <time>          <version>          <desc>
 * 李辉           修改时间           版本号              描述
 */
package com.jk.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 〈一句话功能简述〉<br>
 * 〈aa〉
 *
 * @author 李辉
 * @create 2020/3/16
 * @since 1.0.0
 */
public class ThreadPool {
    private BlockingQueue<Runnable> blockingQueue;

    private List<Thread> worders;

    public static class Worker extends Thread {

        private ThreadPool pool;

        public Worker(ThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            while (this.pool.isworking || this.pool.blockingQueue.size() > 0) {
                Runnable task = null;
                try {
                    if (this.pool.isworking) {
                        Runnable take = this.pool.blockingQueue.take();
                        new Thread(take).start();
                    } else {
                        this.pool.blockingQueue.poll();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    task.run();
                    System.out.println("task" + Thread.currentThread().getName());
                }
            }
        }
    }

    public ThreadPool(int taskSize, int poolSize) {
        if (taskSize <= 0 || poolSize <= 0) {
            throw new IllegalArgumentException("非法参数");
        }
        this.blockingQueue = new LinkedBlockingDeque<>(taskSize);
        this.worders = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(this);
            worker.start();
            worders.add(worker);
        }
    }

    public boolean submit(Runnable task) {
        if (this.isworking) {
            return blockingQueue.offer(task);
        } else {
            return false;
        }
    }

    private volatile Boolean isworking = true;

    public void shutDown() {
        this.isworking = false;
        for (Thread worker : worders) {
            if (worker.getState().equals(Thread.State.BLOCKED)) {
                worker.interrupt();//强制中断
            }
        }
    }

    public static void main(Test1[] args) {
        ThreadPool pool = new ThreadPool(6, 3);
        long l = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
      /* pool.submit(new Runnable() {
            @Override
            public void run() {
                get();
            }
        });*/
            pool.submit(() -> {
                get();
            });
        }
        long f = System.currentTimeMillis();
        System.out.println(f - l);
        pool.shutDown();
    }

    public static void get() {

        int i = -1;
        double a = 10.0;
        System.out.println("执行完毕" + i + a);
    }
}

