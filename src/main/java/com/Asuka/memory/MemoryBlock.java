package com.Asuka.memory;

import lombok.Getter;

@Getter
public class MemoryBlock {
    private Address address;          // 内存块起始位置
    private int endPosition;          // 内存块终止位置
    private int PID;                  // 占用内存的进程序号
    private int length;               // 内存块长度
    private boolean allocated;        // 占用状态


    public MemoryBlock(int startPosition, int length, boolean isEmpty, int PID){
        address = new Address(startPosition, startPosition + length - 1);
        this.endPosition = startPosition + length - 1;
        this.length = length;
        this.allocated = isEmpty;
        this.PID = PID;
    }

    public MemoryBlock(MemoryBlock block,boolean isEmpty){
        this(block.getStartPosition(),block.getLength(),isEmpty, -1);
    }


    public int getStartPosition(){
        return address.getStartAddress();
    }

    public boolean equals(MemoryBlock block){
        return block.getLength() == this.getLength() && block.getStartPosition()==this.getStartPosition();
    }
}
