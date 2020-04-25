package com.wangyang.schedule.test;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduledExecutor {
    public static void main(String[] args) {

    }

    public static void executeAtFixTime() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(new MyTask3(),1, TimeUnit.SECONDS);

        Thread.sleep(10000);
        executorService.shutdown();
    }
}

class MyTask3 extends TimerTask {
    @Override
    public void run() {
        System.out.println("运行时间为"+new Date());
    }
}
