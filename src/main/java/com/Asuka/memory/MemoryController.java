package com.Asuka.memory;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MemoryController {
    private Memory memory;
    private static MemoryController memoryController;
    @Getter
    private List<MemoryBlock> myMemoryBlockList = new ArrayList<>();
    private static final int MAX_SIZE = 511;

    public MemoryController() {
        this.memory = Memory.getInstance();
    }

    public static MemoryController getInstance() {
        if (memoryController == null) {
            memoryController = new MemoryController();
        }
        return memoryController;
    }

    public static void mergeMemory()
    {
        int start=0;
        int length;
        for(int i = 0;i < memoryController.myMemoryBlockList.size(); i++)
        {
            System.out.println("Start="+memoryController.myMemoryBlockList.get(i).getAddress().getStartAddress());
            System.out.println("end="+memoryController.myMemoryBlockList.get(i).getAddress().getEndAddress());
            length=memoryController.myMemoryBlockList.get(i).getAddress().getEndAddress() -
                    memoryController.myMemoryBlockList.get(i).getAddress().getStartAddress();
            memoryController.myMemoryBlockList.get(i).getAddress().setStartAddress(start);
            memoryController.myMemoryBlockList.get(i).getAddress().setEndAddress(start+length);
            System.out.println("Start2="+memoryController.myMemoryBlockList.get(i).getAddress().getStartAddress());
            System.out.println("end2="+memoryController.myMemoryBlockList.get(i).getAddress().getEndAddress());

            start=start+length+1;
        }
    }
}