package com.Asuka.CPU;

import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CPU类模拟CPU运行时间的计算，采用单例模式确保只有一个实例
 */
@ToString
@Getter
public class CPU implements Runnable {
    private static volatile CPU cpu;                         // 单例模式的cpu对象
    private final ReentrantLock lock = new ReentrantLock();  // 定义一个私有的ReentrantLock实例，用于同步控制
    private Instant startTime;                               // 当前开始时间
    private Instant stopTime;                                // 当前结束时间
    private Duration runningTime = Duration.ZERO;            // 运行时间
    private final AtomicBoolean stopFlag = new AtomicBoolean(true); // 是否cpu停止

    /**
     * 获取CPU实例，实现单例模式
     * @return CPU实例
     */
    public static CPU getInstance() {
        if (cpu == null) {
            synchronized (CPU.class) {
                if (cpu == null) {
                    cpu = new CPU();
                }
            }
        }
        return cpu;
    }

    /**
     * 构造方法，启动线程
     */
    private CPU() {
        Thread thread = new Thread(cpu);
        thread.setDaemon(true); // 设置为守护进程
        thread.start();         // 启动线程
    }

    /**
     * 线程运行方法，循环计算CPU运行时间
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!stopFlag.get()) {
                    Instant now = Instant.now();
                    Duration currentDuration = Duration.between(startTime, now);
                    runningTime = runningTime.plus(currentDuration);
                    startTime = now;
                }
            } catch (Exception e) {
                Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, "Error in CPU run method", e);
            }
            try {
                // 隔一段时间再来检测CPU是否运行
                Thread.sleep(10L);
            } catch (InterruptedException var2) {
                Thread.currentThread().interrupt();
                Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, "CPU interrupted", var2);
                break;
            }
        }
    }

    /**
     * 停止CPU计时
     */
    public void stop() {
        lock.lock();
        try {
            stopFlag.set(true);
            stopTime = Instant.now();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 重置CPU计时
     */
    public void reset() {
        lock.lock();
        try {
            stop();
            runningTime = Duration.ZERO;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 检查CPU是否正在运行
     * @return 如果CPU正在运行返回true，否则返回false
     */
    public boolean isRunning() {
        return !stopFlag.get();
    }

    /**
     * 开始CPU计时
     */
    public void play() {
        lock.lock();
        try {
            startTime = Instant.now();
            stopFlag.set(false);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取当前记录的CPU运行时间（秒）
     * @return 当前CPU运行时间（秒）
     */
    public double getRunningTime() {
        lock.lock();
        try {
            return runningTime.getSeconds() + runningTime.toNanosPart() / 1_000_000_000.0;
        } finally {
            lock.unlock();
        }
    }
}
