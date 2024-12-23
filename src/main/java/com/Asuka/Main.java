package com.Asuka;

import com.Asuka.CPU.CPU;
import com.Asuka.ProcessManager.ProcessController;


public class Main {
    public static void main(String[] args) {
        ProcessController processController = ProcessController.getInstance();
        processController.createProcess(1);
        CPU cpu = CPU.getInstance();
        cpu.play();
        System.out.println(cpu.toString());
    }
}
