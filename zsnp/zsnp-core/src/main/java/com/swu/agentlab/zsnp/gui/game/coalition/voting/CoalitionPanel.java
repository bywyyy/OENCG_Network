package com.swu.agentlab.zsnp.gui.game.coalition.voting;

import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;

import javax.swing.*;
import java.awt.*;

/**
 * @author JJ.Wu
 */
public class CoalitionPanel extends JPanel {

    private JLabel lbl_partyNums;

    private JRadioButton rb_select;

    private int rewards;

    public CoalitionPanel(Coalition coalition, ButtonGroup group, SelectedListener listener) {
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        lbl_partyNums = new JLabel(coalition.getPartyNums().toString());
        rb_select = new JRadioButton();
        this.add(lbl_partyNums);
        this.add(rb_select);
        rewards = coalition.getRewards();
        group.add(rb_select);
        rb_select.addActionListener(e -> {
            listener.selected(coalition);
        });
    }

    public void setEnable(boolean enable){
        if(enable){
            this.setForeground(Color.black);
            rb_select.setEnabled(enable);
        }else{
            this.setForeground(Color.gray);
            rb_select.setEnabled(enable);
        }
    }
}
