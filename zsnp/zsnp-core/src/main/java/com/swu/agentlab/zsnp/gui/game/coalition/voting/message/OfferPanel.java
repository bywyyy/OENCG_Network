package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class OfferPanel extends JPanel {

    protected Color backgroundColor;

    protected Font font;

    protected GUIBundle guiBundle;

    public OfferPanel() {

        this.font = new Font("微软雅黑", Font.BOLD, 12);

        this.guiBundle = GUIBundle.getInstance("arena");

    }

    @Override
    protected void paintComponent(Graphics g) {
        if(backgroundColor == null){
            super.paintComponent(g);
        }else{
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20, 20);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(backgroundColor);
            g2d.fill(rect);
            /*RoundRectangle2D.Double borderRect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20, 20);
            g2d.draw(borderRect);*/
        }

        //super.paintComponent(g);
    }
}
