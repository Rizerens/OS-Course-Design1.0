package com.Asuka.ProcessManager.Strategy;

import com.Asuka.ProcessManager.ProcessController;
import com.Asuka.ProcessManager.Process;
import java.util.concurrent.TimeUnit;

public class FCFSStrategy implements SchedulingStrategy {
    @Override
    public void schedule(ProcessController processController) {
        // 检查就绪队列中是否有进程
        if (!processController.getReadyQueue().isEmpty()) {
            // 如果当前运行的是挂起进程
            if (processController.getRunningProcess() == processController.getHangOutProcess()) {
                // 选择就绪队列的第一个进程并设置为运行进程
                Process newProcess = processController.getProcessFromReadyQueue();
                if (newProcess != null) {
                    processController.setRunningProcess(newProcess);
                }
            }
        }

        // 更新运行时间
        processController.setAllRunTime(processController.getAllRunTime() + processController.getRefreshTime());
        // 检查运行进程是否需要切换
        if (processController.getRunningProcess() != processController.getHangOutProcess()) {
            if (processController.getRunningProcess().getPcb().getRemainingTime() <= 0) {
                processController.terminateCurrentProcess();
            } else {
                processController.getRunningProcess().getPcb().setRemainingTime(
                        processController.getRunningProcess().getPcb().getRemainingTime() - processController.getRefreshTime());

            }
        }

    }
}
