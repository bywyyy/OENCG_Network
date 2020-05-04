package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.gui.config.GUIBundle;

import javax.swing.*;
import java.awt.*;

public class SessionBeginPanel extends JPanel {

    private JLabel label;

    private GUIBundle guiBundle;

    public SessionBeginPanel(int sessionNum){
        guiBundle = GUIBundle.getInstance("arena");
        label = new JLabel(guiBundle.formatString("session_begin_tip"), sessionNum);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.add(label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawLine(20, getHeight()/2, getWidth()/2-40, getHeight()/2);
        g.drawLine(getWidth()/2+40, getHeight()/2, getWidth()-20, getHeight()/2);
    }

    public static void main(String[] args) {
        JPanel panel = new SessionBeginPanel(2);
        //panel.setPreferredSize(new Dimension(200,300));
        panel.setBorder(new RoundCornerBorder(Color.red, 50));
        //panel.setBackground(Color.red);
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
