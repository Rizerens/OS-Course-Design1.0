package com.Asuka.CPU;

import lombok.ToString;

import java.util.logging.Level;
import java.util.logging.Logger;

@ToString
public class CPU extends Thread {
	private static CPU cpu;					// 单例模式的cpu对象
	private double time = 0.0D;				// 运行时间
	private boolean stopFlag = true;		// 是否cpu停止

	public static CPU getInstance() {
		if (cpu == null) {
			cpu = new CPU();
		}
		return cpu;
	}

	public CPU() {
		// 设置为守护进程
		this.setDaemon(true);
		this.start();
	}

	public void play() {
	}
}
