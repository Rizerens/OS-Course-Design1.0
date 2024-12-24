package com.Asuka.ProcessManager;

import com.Asuka.CPU.CPU;
import com.Asuka.ProcessManager.Strategy.FCFSStrategy;
import com.Asuka.ProcessManager.Strategy.SchedulingStrategy;
import com.Asuka.memory.Address;
import com.Asuka.utils.processUtils.ProcessUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * 进程控制类，用于管理和调度进程.
 * @author Asuka
 * @since 1.0
 */
@Getter
public class ProcessController implements Runnable {

    private static final String NO_PROCESS = "无";                       // 空进程名
    private static final String EMPTY_QUEUE = "空";                      // 空队列名

    private final List<Process> runningQueue = new ArrayList<>();		// 中断队列
    private final List<Process> readyQueue = new ArrayList<>();		    // 就绪队列
    private final List<Process> blockedQueue = new ArrayList<>();		// 阻塞队列
    @Setter
    private int processIdCounter = 0;                                   // 进程ID计数器
    @Setter
    private double timeChip = 2.0D;								        // 时间片
    @Setter
    private double refreshTime = 0.01D;                                 // 刷新时间
    @Setter
    private double allRunTime = 0D;								        // 轮转运行时间
    private static ProcessController processController;                 // 单例
    private final CPU cpu = CPU.getInstance();
    @Setter
    private Process runningProcess;                                     // 当前运行进程
    private final Process hangOutProcess;                               // 空闲进程
    @Setter
    private boolean startPriority = true;                               // 是否使用优先级调度
    @Setter
    private SchedulingStrategy schedulingStrategy;                      // 调度策略
    /**
     * 获取单例.
     * @return <code>ProcessController</code>的单例实例
     */
    public static ProcessController getInstance() {
        if (processController == null) {
            processController = new ProcessController();
        }
        return processController;
    }

    /**
     * 私有构造函数，用于初始化<code>ProcessController</code>并运行挂起进程.
     */
    private ProcessController() {
        // 默认算法
        this.schedulingStrategy = new FCFSStrategy();
        this.hangOutProcess = new Process(new PCB(-1, "HangOut", 0, 0.0), new Address());
        this.runningProcess = hangOutProcess;
    }

    @Override
    public void run() {
        while (true) {
            schedulingStrategy.schedule(this);
            // 模拟时间流逝，这里可以根据实际需求调整
            try {
                TimeUnit.MILLISECONDS.sleep((long) (processController.getRefreshTime() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建一个新进程，将其添加到就绪队列.
     */
    public void createProcess() {
        PCB pcb = ProcessUtils.getRandomPCB();
        processIdCounter++;
        readyQueue.add(new Process(pcb, new Address()));
    }

    /**
     * 暂停一个进程，将其添加到阻塞队列.
     * @param pid 进程ID，用于标识要暂停的进程
     * @throws IllegalArgumentException 如果未找到指定的进程ID或进程已经在阻塞状态
     */
    public void pauseProcess(int pid) throws IllegalArgumentException {
        // 检查阻塞队列，避免重复阻塞
        if (blockedQueue.stream().anyMatch(process -> process.getPcb().getId() == pid)) {
            throw new IllegalArgumentException("进程 " + pid + " 已经处于阻塞状态");
        }

        // 检查就绪队列
        Optional<Process> processOptional = readyQueue.stream()
                .filter(process -> process.getPcb().getId() == pid)
                .findFirst();
        if (processOptional.isPresent()) {
            Process process = processOptional.get();
            process.getPcb().setState(ProcessState.BLOCKED);
            readyQueue.remove(process);
            blockedQueue.add(process);

            // 如果暂停的是当前运行的进程，则立即选择下一个进程
            if (runningProcess != null && runningProcess.getPcb().getId() == pid) {
                runningProcess = hangOutProcess;
                schedulingStrategy.schedule(this); // 立即调度下一个进程
            }
            return;
        }

        // 检查运行队列
        if (runningProcess != hangOutProcess && runningProcess.getPcb().getId() == pid) {
            runningProcess.getPcb().setState(ProcessState.BLOCKED);
            runningQueue.add(runningProcess); // 如果需要，可以将运行中的进程放到中断队列
            runningProcess = hangOutProcess; // 设置为闲置进程
            blockedQueue.add(runningProcess);
            schedulingStrategy.schedule(this); // 立即调度下一个进程
            return;
        }

        // 如果没有找到指定pid的进程，抛出异常
        throw new IllegalArgumentException("未找到指定的进程ID: " + pid);
    }

    /**
     * 恢复一个进程，将其添加到就绪队列.
     * @param pid 进程ID，用于标识要恢复的进程
     * @throws IllegalArgumentException 如果未找到指定的进程ID
     */
    public void resumeProcess(int pid) throws IllegalArgumentException {
        // 检查阻塞队列
        Optional<Process> processOptional = blockedQueue.stream()
                .filter(process -> process.getPcb().getId() == pid)
                .findFirst();

        if (processOptional.isPresent()) {
            Process process = processOptional.get();
            process.getPcb().setState(ProcessState.READY);
            blockedQueue.remove(process);
            readyQueue.add(process);
        } else {
            throw new IllegalArgumentException("未找到指定的进程ID: " + pid);
        }
    }


    /**
     * 执行进程中断，将原进程放回就绪队列，运行指定进程.
     * @param pid 要设置为运行中的进程的ID
     * @throws IllegalArgumentException 如果未找到指定的进程ID
     */
    public void interruptProcess(int pid) throws IllegalArgumentException {
        // 检查是否当前运行的进程已经是目标进程
        if (runningProcess != hangOutProcess && runningProcess.getPcb().getId() == pid) {
            throw new IllegalArgumentException("当前运行的进程已经是目标进程: " + pid);
        }
        // 在就绪队列中查找指定pid的进程
        Optional<Process> processOptional = readyQueue.stream()
                .filter(process -> process.getPcb().getId() == pid)
                .findFirst();

        // 如果在就绪队列中没有找到，尝试在阻塞队列中查找
        if (processOptional.isEmpty()) {
            processOptional = blockedQueue.stream()
                    .filter(process -> process.getPcb().getId() == pid)
                    .findFirst();
        }

        // 如果在阻塞队列中没有找到，尝试在中断队列中查找
        if (processOptional.isEmpty()) {
            processOptional = runningQueue.stream()
                    .filter(process -> process.getPcb().getId() == pid)
                    .findFirst();
        }

        // 如果找到了指定pid的进程，则与运行中的进程交换位置
        if (processOptional.isPresent()) {
            Process process = processOptional.get();
            runningProcess.getPcb().setState(ProcessState.READY);
            runningQueue.add(runningProcess);
            runningProcess = process;
            process.getPcb().setState(ProcessState.RUNNING);
            schedulingStrategy.schedule(this);
        } else {
            throw new IllegalArgumentException("未找到指定的进程ID: " + pid);
        }
    }




    /**
     * 销毁一个进程，将其从就绪队列或阻塞队列中移除.
     * @param pid 进程ID，用于标识要终止的进程
     */
    public void terminateProcess(int pid) {
        Optional<Process> processOptional = readyQueue.stream()
                .filter(process -> process.getPcb().getId() == pid)
                .findFirst();
        if (processOptional.isPresent()) {
            readyQueue.remove(processOptional.get());
        } else {
            processOptional = blockedQueue.stream()
                    .filter(process -> process.getPcb().getId() == pid)
                    .findFirst();
            processOptional.ifPresent(blockedQueue::remove);
        }
    }

    /**
     * 运行进程结束并运行闲置进程。
     */
    public void terminateCurrentProcess() {
        System.out.println("进程结束PID:" + runningProcess.getPcb().getId());
        terminateProcess(runningProcess.getPcb().getId());
        runningProcess = hangOutProcess;
    }


    /**
     * 从中断队列中获取第一个进程.
     * @return 如果队列为空则返回null，否则返回被抢占队列中的第一个进程
     */
    private Process getProcessFromRunningQueue() {
        if (!runningQueue.isEmpty()) {
            Process process = runningQueue.remove(0);
            process.getPcb().setState(ProcessState.RUNNING);
            return process;
        }
        return null;
    }

    /**
     * 从就绪队列中获取第一个进程.
     * @return 如果就绪队列为空则返回null，否则返回就绪队列中的下一个进程
     */
    public Process getProcessFromReadyQueue() {
        if (!readyQueue.isEmpty()) {
            Process process = readyQueue.get(0); // 从就绪队列中返回第一个进程
            process.getPcb().setState(ProcessState.RUNNING); // 设置进程状态为运行
            runningProcess = process;
            return process;
        } else {
            runningProcess = hangOutProcess;
            return null;
        }
    }



    /**
     * 打印当前系统的进程状态信息，包括运行进程、CPU运行时间、调度算法，
     */
    public String printProcessStates() {
        StringBuilder output = new StringBuilder();
        output.append("====================================================================\n");
        output.append("当前运行进程: ").append(getProcessName(runningProcess)).append("\n");
        output.append("运行时间: ").append(allRunTime).append("\n");
        output.append("调度算法: ").append(schedulingStrategy != null ? schedulingStrategy.getClass().getSimpleName() : NO_PROCESS).append("\n");
        output.append(CPU.getInstance().getRunningTime()).append("\n");

        appendQueueInfo(output, "就绪队列", readyQueue);
        appendQueueInfo(output, "阻塞队列", blockedQueue);
        appendQueueInfo(output, "中断队列", runningQueue);

        output.append("====================================================================");
        return output.toString();
    }


    /**
     * 获取当前运行进程，
     * @return 当前运行进程的名称，如果没有运行进程，则返回"无"。
     */
    private String getProcessName(Process process) {
        return process != null && process.getPcb() != null ? process.getPcb().getName() : NO_PROCESS;
    }

    /**
     * 将队列信息添加到输出字符串中，包括队列名称、队列中的进程信息等。
     * @param output 输出字符串的引用，用于添加队列信息。
     * @param queueName 队列名称，用于标识队列。
     * @param queue 队列对象，包含进程信息。
     */
    private void appendQueueInfo(StringBuilder output, String queueName, List<Process> queue) {
        output.append(queueName).append(":\n");
        if (queue.isEmpty()) {
            output.append(EMPTY_QUEUE).append("\n");
        } else {
            synchronized (queue) {
                for (Process process : queue) {
                    PCB pcb = process.getPcb();
                    if (pcb != null) {
                        output.append(String.format("ID: %d, 名称: %s, 状态: %s, 优先级: %d, 剩余时间: %.2f, 总时间: %.2f\n",
                                pcb.getId(),
                                pcb.getName(),
                                pcb.getState(),
                                pcb.getPriority(),
                                pcb.getRemainingTime(),
                                pcb.getTotalRunTime())
                        );
                    }
                }
            }
        }
    }


}
