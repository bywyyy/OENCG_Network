package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProposalActionPanel extends ProposalPanel {

    private JButton btn_reject;

    private JButton btn_agree;

    private JPanel panel;

    public ProposalActionPanel(Proposal proposal) {
        super(proposal);
        btn_reject = new JButton("Reject");
        btn_agree = new JButton("Agree");
        this.addButton();
    }

    public void addButton(){
        panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        panel.setOpaque(false);
        panel.add(btn_reject);
        panel.add(btn_agree);
        this.add(panel);
    }

    public void addListener(ActionListener rejectListener, ActionListener agreeListener){
        btn_reject.addActionListener(rejectListener);
        btn_agree.addActionListener(agreeListener);
        btn_reject.addActionListener(e -> {
            removeButton();
        });
        btn_agree.addActionListener(e -> {
            removeButton();
        });
    }

    public void removeButton(){
        btn_reject.setEnabled(false);
        btn_agree.setEnabled(false);
        this.remove(panel);
    }
}
