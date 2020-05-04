package com.swu.agentlab.zsnp.gui.game.coalition.voting;

import javax.swing.border.Border;
import java.awt.*;

public class FlickerBorder implements Border {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

    }

    @Override
    public Insets getBorderInsets(Component c) {
        return null;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public void flick(){}
}
