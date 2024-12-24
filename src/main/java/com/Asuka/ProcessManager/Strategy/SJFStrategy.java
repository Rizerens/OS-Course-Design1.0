package com.Asuka.ProcessManager.Strategy;

import com.Asuka.ProcessManager.Process;
import com.Asuka.ProcessManager.ProcessController;

public class SJFStrategy implements SchedulingStrategy {
    @Override
    //实现SJF短作业
    public void schedule(ProcessController processController) {
        // 检查就绪，中断队列中是否有进程
        if (!processController.getReadyQueue().isEmpty()) {
            if (processController.getRunningProcess() == processController.getHangOutProcess()) {
                // 选择就绪队列最短的时间进程并设置为运行进程，不用interface函数
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
