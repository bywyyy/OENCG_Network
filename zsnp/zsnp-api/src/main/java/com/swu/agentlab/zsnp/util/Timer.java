package com.swu.agentlab.zsnp.util;

public class Timer {

    public static void Countdown(int seconds, TimeLaseHandler timeLaseHandler, TimeOutHandler timeOutHandler){
        while(seconds>0){
            timeLaseHandler.timeLapse(seconds);
            try {
                Thread.sleep(1000);
                seconds -- ;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timeOutHandler.timeOut();
    }

    /**
     * 倒计时结束处理器
     */
    public interface TimeOutHandler{

        void timeOut();

    }

    public interface TimeLaseHandler{

        void timeLapse(int restTime);

    }

    public static void main(String[] args) {
        Timer.Countdown(5, restTime -> {
                    System.out.println(restTime);
                },
            ()->{
                System.exit(0);
            });
    }
}
