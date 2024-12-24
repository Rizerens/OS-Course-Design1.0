package com.Asuka.ProcessManager;

import com.Asuka.memory.Address;
import lombok.*;

/**
 * 进程类，用于表示进程。
 *  @author Asuka
 *  @since  1.0
 */

@Getter
@AllArgsConstructor
public class Process {
    private PCB pcb;                            // 进程控制块
    private Address address;                    // 进程地址
}
