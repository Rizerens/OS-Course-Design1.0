package com.Asuka.ProcessManager.Strategy;

import com.Asuka.ProcessManager.ProcessController;

public interface SchedulingStrategy {
    void schedule(ProcessController processController);
}
