package com.Asuka.ProcessManager.Scheduler;

import java.util.List;

public class FCFS implements Scheduler {
    @Override
    public Process selectNextProcess(List<Process> processes) {
        if (processes.isEmpty()) {
            return null;
        }
        return processes.get(0);
    }
}
