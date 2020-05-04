package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;

import javax.swing.*;
import java.awt.*;

public class ResponsePanel extends OfferPanel {

    public ResponsePanel(Response response) {
        JLabel label = new JLabel();
        label.setFont(this.font);
        RoundCornerBorder border = new RoundCornerBorder(20);
        if(response.isAgree()){
            label.setText(guiBundle.getString("response_agree"));
            this.add(label);
            this.backgroundColor = Color.green;
            border.setColor(backgroundColor);
        }else{
            label.setText(guiBundle.getString("response_refuse"));
            this.add(label);
            this.backgroundColor = Color.yellow;
            border.setColor(backgroundColor);
        }
        this.setBorder(border);
    }

}
