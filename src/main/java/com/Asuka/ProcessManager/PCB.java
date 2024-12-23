package com.Asuka.ProcessManager;

import lombok.*;

/**
 * 进程状态枚举类
 */
enum ProcessState {
    READY, RUNNING, BLOCKED, TERMINATED
}

/**
 * 进程控制块类，用于描述操作系统中的进程信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PCB {
    private int id;                            // 唯一标识
    private String name;                       // 进程名
    private ProcessState state;                // 进程状态
    private int priority;                      // 优先级
    private Double remainingTime;              // 剩余运行时间
    private Double totalRunTime;               // 总运行时间
    private Double lastRestRunTime;            // 时间片剩余时间
    private boolean isInterrupt;               // 是否中断

    /**
     * 构造函数，用于创建进程控制块
     *
     * @param id          进程唯一标识
     * @param name        进程名
     * @param priority    进程优先级
     * @param remainingTime 程序剩余运行时间
     */
    public PCB(int id, String name, int priority, Double remainingTime) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.remainingTime = remainingTime;
        this.totalRunTime = remainingTime;
        // CPU赋值
        this.lastRestRunTime = remainingTime;
        this.state = ProcessState.READY; // 初始化状态为READY
        this.isInterrupt = false; // 初始化中断标志为false
    }
}
