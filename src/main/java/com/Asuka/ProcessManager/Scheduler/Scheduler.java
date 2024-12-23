package com.Asuka.ProcessManager.Scheduler;

import java.util.List;

public interface Scheduler {
    Process selectNextProcess(List<Process> processes);
}
