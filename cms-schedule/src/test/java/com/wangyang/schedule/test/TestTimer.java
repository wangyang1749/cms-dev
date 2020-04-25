package com.wangyang.schedule.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {

    public static void main(String[] args) throws InterruptedException {
        MyTask myTask = new MyTask();
        Timer timer = new Timer();
        timer.schedule(myTask,1000,2000);
        Thread.sleep(10000);
        myTask.cancel();
        System.out.println("===============");

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND,now.get(Calendar.SECOND)+3);
        Date runDate = now.getTime();
        MyTask2 myTask2 = new MyTask2();
        timer.schedule(myTask2,runDate,3000);
        Thread.sleep(20000);
        timer.cancel();
    }
}

class MyTask extends TimerTask{
    @Override
    public void run() {
        System.out.println("运行时间为"+new Date());
    }
}

class MyTask2 extends  TimerTask{
    @Override
    public void run() {
        System.out.println("Run"+new Date());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}