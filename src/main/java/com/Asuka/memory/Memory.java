package com.Asuka.memory;

import com.Asuka.ProcessManager.PCB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Memory {
    private static Memory memory;
    private static List<PCB> pcbList = new ArrayList<>();
    private LinkedList<MemoryBlock> blocks;  //链式存储

    private static final int MEMORY_SIZE =511;

    static{memory = new Memory();}

    public static Memory getInstance() {return memory;}

    private Memory(){
        blocks = new LinkedList<>();
    }

    public List<PCB> getPCBList() {
        return pcbList;
    }

}