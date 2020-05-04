package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.SessionResult;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

public class SessionResultPanel extends OfferPanel {

    public SessionResultPanel(SessionResult sessionResult){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Map<String, String> map = sessionResult.getResults();
        for(String key: map.keySet()){
            JLabel label = new JLabel(key+"   "+map.get(key));
            label.setForeground(Color.gray);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            this.add(label);
        }
        String resultStatus;
        if(sessionResult.isAgree()){
            resultStatus = guiBundle.getString("session_result_title_succeed");
        }else{
            resultStatus = guiBundle.getString("session_result_title_failed");
        }
        TitledBorder border = BorderFactory.createTitledBorder(new RoundCornerBorder(Color.gray, 20),
                guiBundle.getString("session_result_title") + "(" + resultStatus + ")");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitleColor(Color.gray);
        border.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.setBorder(border);
        this.setOpaque(false);
    }

}
