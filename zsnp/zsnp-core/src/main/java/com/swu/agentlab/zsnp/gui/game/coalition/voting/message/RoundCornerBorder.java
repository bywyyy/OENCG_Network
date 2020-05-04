package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RoundCornerBorder implements Border {
    protected int m_w = 6;
    protected int m_h = 6;
    protected Color color;
    protected int radius;

    public RoundCornerBorder(int radius){
        this.radius = radius;
    }

    public RoundCornerBorder(Color color, int radius){
        this.color = color;
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(m_h, m_w, m_h, m_w);
    }
    @Override
    public boolean isBorderOpaque(){
        return true;
    }
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        w = w - 1;
        h = h - 1;
        g.setColor(this.color);
        g.drawRoundRect(x, y, w, h, radius, radius);
        x ++;
        y ++;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200,300));
        panel.setBorder(new RoundCornerBorder(Color.red, 50));
        //panel.setBackground(Color.red);
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
