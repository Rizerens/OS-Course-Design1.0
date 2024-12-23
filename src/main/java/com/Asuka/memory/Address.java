package com.Asuka.memory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 地址范围类，用于描述内存或地址区间。
 */
@Getter
@Setter
@AllArgsConstructor
public class Address {
    private int startAddress;               // 起始地址
    private int endAddress;                 // 结束地址


    public Address() {

    }
}
