package com.bfd.utils;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author: bing.shen
 * @date: 2018/10/24 15:22
 * @Description:
 */
public class Test {

    private static Semaphore semaphore = new Semaphore(5, true);

    public static void main(String[] args) {
        //创建包含20个线程的线程池
        /*ExecutorService executorService = Executors.newFixedThreadPool(20);
        //投递20个任务。
        for (int i = 0; i < 20; i++) {
            final int taskid = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //System.out.println("taskid:" + taskid + " is running...");
                    //获取许可。
                    try {
                        semaphore.acquire();
                        //availablePermits:当前剩余的可用许可。
                        System.out.println(taskid + " Acquired: available:" + semaphore.availablePermits()
                                + " , " + Thread.currentThread().getName()
                                + " , " + new Date());
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;

                    }
                    //释放许可。
                    semaphore.release();
                    *//*System.out.println(taskid + " Released: available:" + semaphore.availablePermits()
                            + " , " + Thread.currentThread().getName()
                            + " , " + new Date());*//*
                }
            });
        }
        executorService.shutdown();*/

        String firstLetter = "id".substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + "id".substring(1);
        System.out.println(getter);

    }

}
