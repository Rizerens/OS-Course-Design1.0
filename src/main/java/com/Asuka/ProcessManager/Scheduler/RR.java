package com.Asuka.ProcessManager.Scheduler;

import java.util.List;

public class RR implements Scheduler {
    private int timeSliceIndex = 0;
    @Override
    public Process selectNextProcess(List<Process> processes) {
        if (processes.isEmpty()) {
            return null;
        }
        if (timeSliceIndex >= processes.size()) {
            timeSliceIndex = 0;
        }
        return processes.get(timeSliceIndex++);
    }
}
