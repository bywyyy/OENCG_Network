package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.SessionEnd;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;


public class SessionEndPanel extends OfferPanel {

    public SessionEndPanel(SessionEnd sessionEnd){
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        Map<PlayerInfo, Double> map = sessionEnd.getRewards();
        for(PlayerInfo playerInfo: map.keySet()){
            JLabel label = new JLabel(playerInfo.getName()
                +"("+playerInfo.getRoleInfo().getRoleName()+")   "
                +map.get(playerInfo));
            label.setForeground(Color.gray);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            this.add(label);
        }
        TitledBorder border = BorderFactory.createTitledBorder(new RoundCornerBorder(Color.gray, 20), "Session "+sessionEnd.getSessionNum());
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitleColor(Color.gray);
        border.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.setBorder(border);
        this.setOpaque(false);
    }

}
