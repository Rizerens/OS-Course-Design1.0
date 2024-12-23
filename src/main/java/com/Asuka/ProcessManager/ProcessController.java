package com.Asuka.ProcessManager;

import com.Asuka.ProcessManager.Scheduler.RR;
import com.Asuka.ProcessManager.Scheduler.Scheduler;
import com.Asuka.memory.Address;
import com.Asuka.utils.processUtils.ProcessUtils;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ProcessController {

    private List<Process> runningQueue = new ArrayList<>();		// 被中断队列
    private List<Process> readyQueue = new ArrayList<>();		// 就绪队列
    private List<Process> blockedQueue = new ArrayList<>();		// 阻塞队列
    private double timeChip = 2.0D;								// 时间片
    private double allRunTime = 0D;								// 轮转运行时间

    private static ProcessController processController;         // 单例
    private Process runningProcess;                             // 当前运行进程
    @Setter
    private Scheduler scheduler;                                // 调度算法
    public static ProcessController getInstance() {
        if (processController == null) {
            processController = new ProcessController();
        }
        return processController;
    }


    private ProcessController() {
        // 默认使用时间片轮转算法
        this.scheduler = new RR();
    }

    // 创建进程
    public void createProcess(int pid) {
        String name = ProcessUtils.getRandomProcessName();
        int priority = ProcessUtils.getRandomPriority();
        double remainingTime = ProcessUtils.getRandomRemainingTime();
        PCB pcb  = new PCB(pid, name, priority, remainingTime);
        readyQueue.add(new Process(pcb, new Address()));
    }

    // 暂停进程
    public void pauseProcess(int pid) {
        Optional<Process> processOptional = readyQueue.stream()
                .filter(process -> process.getPcb().getId() == pid)
                .findFirst();
        if (processOptional.isPresent()) {
            Process process = processOptional.get();
            process.getPcb().setState(ProcessState.BLOCKED); // 将进程状态设为阻塞
            readyQueue.remove(process);
            blockedQueue.add(process);
        }
    }

    // 恢复进程
    public void resumeProcess(int pid) {
        Optional<Process> processOptional = blockedQueue.stream()
                .filter(process -> process.getPcb().getId() == pid)
                .findFirst();
        if (processOptional.isPresent()) {
            Process process = processOptional.get();
            process.getPcb().setState(ProcessState.READY); // 将进程状态设为就绪
            blockedQueue.remove(process);
            readyQueue.add(process);
        }
    }

    // 终止进程
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
            processOptional.ifPresent(process -> blockedQueue.remove(process));
        }
    }

    // 输出进程状态
    public void printProcessStates() {
        System.out.println("====================================================================");
        System.out.println("当前运行进程: " + (runningProcess!= null? runningProcess.getPcb().getName(): "无"));
        System.out.println("就绪队列:");
        if (readyQueue == null || readyQueue.isEmpty()){ System.out.println("空");}
        for (Process process : readyQueue) {
            System.out.println(process.getPcb().getName());
        }
        System.out.println("阻塞队列:");
        if (blockedQueue == null || blockedQueue.isEmpty()){ System.out.println("空");}

        for (Process process : blockedQueue) {
            System.out.println(process.getPcb().getName());
        }
        System.out.println("====================================================================");
    }
}
