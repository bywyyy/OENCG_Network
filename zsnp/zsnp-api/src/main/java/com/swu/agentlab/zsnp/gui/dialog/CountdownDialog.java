package com.swu.agentlab.zsnp.gui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CountdownDialog extends JDialog {

    private int seconds;

    private JLabel lbl_sec;

    public CountdownDialog(Frame parent, String tip, int seconds){
        //setModal(true);
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setSize(300, 80);
        setTitle("Warning");
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(0, 1, 0, 5);
        panel.setLayout(layout);
        setContentPane(panel);
        JLabel label = new JLabel(tip, JLabel.CENTER);
        label.setForeground(Color.gray);
        panel.add(label);
        this.seconds = seconds;
        lbl_sec = new JLabel(seconds+"", JLabel.CENTER);
        panel.add(lbl_sec);
        this.setVisible(true);
    }

    public void setSeconds(int seconds){
        this.seconds = seconds;
        lbl_sec.setText(seconds+"");
        lbl_sec.updateUI();
    }
}
