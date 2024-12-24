package com.Asuka;

import com.Asuka.ProcessManager.ProcessController;
import com.Asuka.ProcessManager.Scheduler.FCFS;
import com.Asuka.ProcessManager.Scheduler.RR;
import com.Asuka.ProcessManager.Scheduler.Scheduler;
import com.Asuka.ProcessManager.Scheduler.SJF;
import com.Asuka.ProcessManager.Strategy.FCFSStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static ProcessController processController;
    private static JTextArea processStateTextArea;
    private static Timer timer;

    public static void main(String[] args) {
        // 初始化进程控制器
        processController = ProcessController.getInstance();
        processController.setTimeChip(2.0D);
        processController.setSchedulingStrategy(new FCFSStrategy()); // 默认使用FCFS调度算法
        Thread thread = new Thread(processController);
        thread.setDaemon(true); // 设置为守护进程
        thread.start();         // 启动线程

        // 创建主窗口
        JFrame frame = new JFrame("进程管理器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("添加进程");
        JButton pauseButton = new JButton("阻塞进程");
        JButton interruptButton = new JButton("中断进程");
        JButton resumeButton = new JButton("恢复进程");
        JButton refreshButton = new JButton("刷新状态");

        buttonPanel.add(addButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(interruptButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(refreshButton);

        // 创建文本区域用于显示进程状态
        processStateTextArea = new JTextArea();
        processStateTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(processStateTextArea);

        // 添加组件到主窗口
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 设置按钮事件监听器
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processController.createProcess();
                updateProcessStates();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pidStr = JOptionPane.showInputDialog(frame, "请输入要阻塞的进程ID:");
                if (pidStr != null) {
                    try {
                        int pid = Integer.parseInt(pidStr);
                        processController.pauseProcess(pid);
                        updateProcessStates();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "请输入有效的进程ID", "错误", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



        interruptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pidStr = JOptionPane.showInputDialog(frame, "请输入要中断的进程ID:");
                if (pidStr != null) {
                    try {
                        int pid = Integer.parseInt(pidStr);
                        processController.interruptProcess(pid);
                        updateProcessStates();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "请输入有效的进程ID", "错误", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pidStr = JOptionPane.showInputDialog(frame, "请输入要恢复的进程ID:");
                if (pidStr != null) {
                    try {
                        int pid = Integer.parseInt(pidStr);
                        processController.resumeProcess(pid);
                        updateProcessStates();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "请输入有效的进程ID", "错误", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });




        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProcessStates();
            }
        });

        // 设置定时器定期更新进程状态
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProcessStates();
            }
        });
        timer.start();

        // 显示主窗口
        frame.setVisible(true);
    }

    private static void updateProcessStates() {
        processStateTextArea.setText(processController.printProcessStates());
    }
}
