package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ProposalPanel extends OfferPanel {

    public ProposalPanel(Proposal proposal){
        /*int rowCount = proposal.getAlliesz().size()+1;
        String[] header = {"PartyName", "PlayerName", "Reward"};
        String[][] cont = new String[rowCount][3];*/
        GridLayout gridLayout = new GridLayout(0, 1);
        this.setLayout(gridLayout);
        for(PlayerInfo item: proposal.getProposerz().keySet()){
            JPanel panel = new JPanel();
            BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
            panel.setLayout(layout);
            panel.setOpaque(false);
            JLabel lbl_proposer = new JLabel(item.getName()+"("+item.getRoleInfo().getRoleName()+")");
            lbl_proposer.setFont(this.font);
            panel.add(lbl_proposer);
            panel.add(Box.createHorizontalStrut(20));
            JLabel lbl_reward = new JLabel(proposal.getProposerz().get(item)+"");
            lbl_reward.setFont(this.font);
            panel.add(lbl_reward);
            this.add(panel);
            /*cont[0][0] = item.getRoleInfo().getRoleName();
            cont[0][1] = item.getName();
            cont[0][2] = proposal.getProposerz().get(item)+"";*/
        }
        Map<PlayerInfo, Toallyz> allies = proposal.getAlliesz();
        for(PlayerInfo item: allies.keySet()){
            JPanel panel = new JPanel();
            BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
            panel.setLayout(layout);
            panel.setOpaque(false);
            JLabel lbl_ally = new JLabel(item.getName()+"("+item.getRoleInfo().getRoleName()+")");
            lbl_ally.setFont(this.font);
            panel.add(lbl_ally);
            panel.add(Box.createHorizontalStrut(20));
            JLabel lbl_reward = new JLabel(allies.get(item).getReward()+"");
            lbl_reward.setFont(this.font);
            panel.add(lbl_reward);
            this.add(panel);
        }
        this.backgroundColor = Color.lightGray;
        this.setBorder(new RoundCornerBorder(this.backgroundColor, 20));
        //this.setBorder(new RoundCornerBorder(Color.gray, 20));
    }

}
