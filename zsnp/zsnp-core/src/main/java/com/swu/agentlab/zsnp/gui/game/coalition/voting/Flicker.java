package com.swu.agentlab.zsnp.gui.game.coalition.voting;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Flicker extends JFrame {

    private JButton button;

    public Flicker() throws HeadlessException {
        JPanel panel = new JPanel();
        button = new JButton("按钮");
        panel.add(button);
        this.setContentPane(panel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Flicker flicker = new Flicker();
    }
}
