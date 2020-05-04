package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.GameResult;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

public class GameResultPanel extends OfferPanel {

    public GameResultPanel(GameResult gameResult){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Map<PlayerInfo, Double> results = gameResult.getResults();
        for(PlayerInfo item: results.keySet()){
            JLabel label = new JLabel(item.getName()+"("+item.getRoleInfo().getRoleName()+")    "+results.get(item));
            label.setForeground(Color.darkGray);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            this.add(label);
        }
        TitledBorder border = BorderFactory.createTitledBorder(new RoundCornerBorder(Color.darkGray, 20), guiBundle.getString("game_result"));
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitleColor(Color.darkGray);
        border.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.setBorder(border);
        this.setOpaque(false);
    }

}
