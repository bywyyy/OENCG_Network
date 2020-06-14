package com.swu.agentlab.zsnp.game.coalition.voting.controller.impl;

import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.xmlrec.GameRec;

public class GameRecController {

    private GameRec gameRec;

    public GameRecController(){
        gameRec = new GameRec();
    }

    public void setRoomInfo(String id, String name, int majority){
        gameRec.setRoomInfo(id, name, majority);
    }

    public void addPlayer(Player player){
        gameRec.addPlayer(player.getName(), player.getDescription(), player.getRole().getRoleName(), player.getType()+"", player.getPath());
    }

    public void addKnowledges(String[] roleName, boolean[][] knowledges, int resource[], double talent[]){
        for(int i = 0; i<knowledges.length; i++){
            gameRec.addPartyKnowledge(i, roleName[i], knowledges[i], resource[i], talent[i]);
        }
    }

    public void addCounterBody(CounterBody body){
        int sessionNum = body.getSessionNum();
        int roundNum = body.getRoundNum();
        Offer offer = body.getOffer();
        if(offer instanceof Proposal){
            Proposal proposal = (Proposal) offer;
            for(PlayerInfo item: proposal.getProposerz().keySet()){
                gameRec.addProposal(sessionNum, roundNum, item.getName(), item.getRoleInfo().getRoleName(), proposal.getProposerz().get(item));
            }
            for(PlayerInfo item: proposal.getAlliesz().keySet()){
                gameRec.addAlly(sessionNum, roundNum, item.getName(), item.getRoleInfo().getRoleName(), proposal.getAlliesz().get(item).getReward());
            }
        }else if(offer instanceof Response){
            Response response = (Response) offer;
            gameRec.addResponse(sessionNum, roundNum, response.getPlayerInfo().getName(), response.getPlayerInfo().getRoleInfo().getRoleName(), response.isAgree());
        }
    }

    public void save(){
        gameRec.save();
    }
    public void save(int abc){
        gameRec.save(abc);
    }
}
