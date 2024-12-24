package com.Asuka.CPU;

import org.junit.jupiter.api.Test;

class CPUTest {

    @org.junit.jupiter.api.Test
    void getInstance() {
    }

    @org.junit.jupiter.api.Test
    void run() {
    }

    @org.junit.jupiter.api.Test
    void stop() {
    }

    @org.junit.jupiter.api.Test
    void reset() {
    }

    @org.junit.jupiter.api.Test
    void isRunning() {
    }

    @org.junit.jupiter.api.Test
    void play() {
    }

    @org.junit.jupiter.api.Test
    void getRunningTime() {
    }

    @Test
    void testCPU(){
        CPU cpu = CPU.getInstance();
        cpu.play();
        //10秒关闭CPU
        for (int i = 0; i < 10; i++) {
            if (i == 5){
                cpu.stop();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                cpu.play();
                System.out.println(cpu.toString());
                continue;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(cpu.toString());
        }

        cpu.stop();
    }
}