package com.swu.agentlab.zsnp.gui.game.coalition.voting.message;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.*;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {

    private final Logger log = Logger.getLogger(MessagePanel.class);

    private JLabel lbl_sender;

    private OfferPanel offerPanel;

    private GUIBundle guiBundle;

    public MessagePanel(Offer offer){
        this.guiBundle = GUIBundle.getInstance("arena");
        FlowLayout layout = new FlowLayout();
        //BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        lbl_sender = new JLabel();
        lbl_sender.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        //lbl_sender.setHorizontalTextPosition(SwingConstants.CENTER);
        if(offer instanceof Proposal){
            Proposal proposal = (Proposal) offer;
            for(PlayerInfo playerInfo: proposal.getProposerz().keySet()){
                //lbl_sender.setText(playerInfo.getName()+"("+playerInfo.getRoleInfo().getRoleName()+")");
                this.fillSenderLabel(lbl_sender, playerInfo.getName(), playerInfo.getRoleInfo().getRoleName());
            }
            layout.setAlignment(SwingConstants.EAST);
            this.add(lbl_sender);
            offerPanel = new ProposalPanel((Proposal) offer);
            //offerPanel = new ProposalActionPanel((Proposal) offer);
            this.add(offerPanel);
        }else if(offer instanceof Response){
            Response response = (Response) offer;
            offerPanel = new ResponsePanel(response);
            layout.setAlignment(SwingConstants.RIGHT);
            this.add(offerPanel);
            PlayerInfo playerInfo = response.getPlayerInfo();
            //lbl_sender.setText(playerInfo.getName()+"("+playerInfo.getRoleInfo().getRoleName()+")");
            this.fillSenderLabel(lbl_sender, playerInfo.getName(), playerInfo.getRoleInfo().getRoleName());
            this.add(lbl_sender);
        }else if(offer instanceof SessionEnd){
            SessionEnd sessionEnd = (SessionEnd) offer;
            SessionEndPanel sessionEndPanel = new SessionEndPanel(sessionEnd);
            this.add(sessionEndPanel);
        }else if(offer instanceof SessionBegin){
            SessionBegin sessionBegin = (SessionBegin) offer;
            JLabel label = new JLabel(guiBundle.formatString("session_begin_tip", sessionBegin.getSessionNum()));
            label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            label.setForeground(Color.gray);
            this.add(label);
        }else if(offer instanceof SessionResult){
            //log.info(offer);
            SessionResult sessionResult = (SessionResult) offer;
            SessionResultPanel sessionResultPanel = new SessionResultPanel(sessionResult);
            this.add(sessionResultPanel);
        }else if(offer instanceof GameResult){
            GameResult gameResult = (GameResult) offer;
            GameResultPanel gameResultPanel = new GameResultPanel(gameResult);
            this.add(gameResultPanel);
        }
        this.setOpaque(false);
    }

    private void fillSenderLabel(JLabel label, String name, String party){
        int len = name.length() + party.length();
        if(len<=10){
            label.setText("<html><body>"
                    +name
                    +"("
                    +party
                    +")</body></html>");
        }else{
            label.setText("<html><body>"
                    +name
                    +"<br>("
                    +party
                    +")</body></html>");
        }

        /*if(len<=4){
            label.setText("<html><body>"
                    +name
                    +"<br>("
                    +party
                    +")</body></html>");
        }else{
            int split = len/2;
            String str1 = name.substring(0, split);
            String str2 = name.substring(split, len);
            label.setText("<html><body>"
                    +str1
                    +"<br>"
                    +str2
                    +"<br>("
                    +party
                    +")</body></html>");
        }*/
    }

}
