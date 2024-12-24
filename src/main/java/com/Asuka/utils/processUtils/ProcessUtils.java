package com.Asuka.utils.processUtils;

import com.Asuka.ProcessManager.PCB;
import com.Asuka.ProcessManager.ProcessController;

import java.util.Random;

/**
 * ProcessUtils类提供了进程相关的实用方法
 */
public class ProcessUtils {

    /**
     * 进程优先级
     */
    public static final int MAX_PRIORITY = 5;                            //最高的进程优先级
    public static final int MIN_PRIORITY = 0;                            //最低的进程优先级
    private static final Random random = new Random();                   //用于生成随机数的Random对象

    /**
     * 剩余运行时间范围
     */
    public static final double MIN_REMAINING_TIME = 1;                  // 最小剩余运行时间（秒）
    public static final double MAX_REMAINING_TIME = 5;                  // 最大剩余运行时间（秒）

    /**
     * 进程名称
     */
    private static final String[] PROCESS_NAMES = {
        "ProcessA", "ProcessB", "ProcessC", "ProcessD", "ProcessE",
        "ProcessF", "ProcessG", "ProcessH", "ProcessI", "ProcessJ"
    };                                                                  //进程名称数组，包含可能的进程名称

    /**
     * 获取随机进程优先级
     *
     * @return 随机生成的进程优先级，范围在MIN_PRIORITY到MAX_PRIORITY之间
     */
    public static int getRandomPriority() {
        return random.nextInt(MAX_PRIORITY - MIN_PRIORITY + 1) + MIN_PRIORITY;
    }

    /**
     * 获取随机进程名称
     *
     * @return 从PROCESS_NAMES数组中随机选择的一个进程名称
     */
    public static String getRandomProcessName() {
        return PROCESS_NAMES[random.nextInt(PROCESS_NAMES.length)];
    }

    /**
     * 获取随机运行时间
     *
     * @return 随机生成运行时间，范围在MIN_REMAINING_TIME到MAX_REMAINING_TIME之间（秒）
     */
    public static double getRandomRemainingTime() {
        return random.nextDouble() * (MAX_REMAINING_TIME - MIN_REMAINING_TIME) + MIN_REMAINING_TIME;
    }

    /**
     * 获取随机生成的PCB对象
     * @return 包含随机优先级、名称和剩余运行时间的PCB对象
     */
    public static PCB getRandomPCB() {
        int priority = getRandomPriority();
        String name = getRandomProcessName();
        double remainingTime = getRandomRemainingTime();
        return new PCB(ProcessController.getInstance().getProcessIdCounter(), name, priority, remainingTime);
    }

}
