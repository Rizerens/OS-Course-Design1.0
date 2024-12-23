package com.Asuka.ProcessManager;

import com.Asuka.memory.Address;
import lombok.*;

/**
 * 进程类，用于表示操作系统中的进程。
 */
@Setter
@Getter
@AllArgsConstructor
public class Process {
    private PCB pcb;                            // 进程控制块
    private Address address;                    // 进程地址
}
