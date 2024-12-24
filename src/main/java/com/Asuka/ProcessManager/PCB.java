package com.Asuka.ProcessManager;

import lombok.*;

/**
 * 进程状态枚举，用于枚举进程状态
 *  @author Asuka
 *  @since  1.0
 */
enum ProcessState {
    READY, RUNNING, BLOCKED, TERMINATED
}

/**
 * 进程控制块类，用于描述详细的进程信息
 * @author Asuka
 * @since  1.0
 */
@AllArgsConstructor
@Getter
public class PCB {
    private int id;                            // 唯一标识
    private String name;                       // 进程名
    private Double totalRunTime;               // 总运行时间
    @Setter
    private ProcessState state;                // 进程状态
    @Setter
    private int priority;                      // 优先级
    @Setter
    private Double remainingTime;              // 剩余运行时间(模拟不一的长短作业)
    @Setter
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
        this.totalRunTime = remainingTime;        // CPU赋值
        this.state = ProcessState.READY;
        this.isInterrupt = false;
    }
}

