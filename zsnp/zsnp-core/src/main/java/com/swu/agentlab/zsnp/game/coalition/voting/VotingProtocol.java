package com.swu.agentlab.zsnp.game.coalition.voting;

import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Round;
import lombok.Data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author JJ.Wu
 */
@Data
public class VotingProtocol extends Protocol {

    private LinkedList<String> playerIds;

    private VotingDomain domain;

    private Round round;

    private boolean roundEnd;

    private boolean sessionEnd;

    private boolean gameEnd;

    private int sessionAmounts;

    private int sessionNum;

    private int roundNum;               //总的回合数，所有场

    private int maxRound;               //每一场最大的回合数

    private int sessionRoundNum;        //当前场次的回合数

    private boolean isAgree;

    public VotingProtocol(){
        this.round = new Round();
        this.sessionAmounts = 3;
        this.sessionNum = 1;
        this.roundNum = 1;
        this.roundEnd = false;
        this.sessionEnd =false;
    }

    public VotingProtocol(LinkedList playerIds, VotingDomain domain){
        this.playerIds = playerIds;
        this.domain = domain;
        this.round = new Round();
    }

    @Override
    public synchronized Set nextSpeakers() {
        Set nextSpeakers = new HashSet();
        /*String currSpeaker = null;
        for(PlayerInfo item: round.getProposal().getProposerz().keySet()){
            currSpeaker = item.getId();
        }
        int currIndex = playerIds.indexOf(currSpeaker);
        nextSpeakers.add(playerIds.get((currIndex+1)%playerIds.size()));*/
        nextSpeakers.add(playerIds.get((roundNum - 1)%playerIds.size()));
        return nextSpeakers;
    }

    @Override
    public void receiveCounterBody(CounterBody counterBody) {
        super.receiveCounterBody(counterBody);
        counterBody.setSessionNum(this.sessionNum);
        counterBody.setRoundNum(this.roundNum);
        this.updateRound();
    }

    @Override
    public void receiveCommunicateBody(CommunicateBody communicateBody) {
        super.receiveCommunicateBody(communicateBody);
        communicateBody.setSessionNum(this.sessionNum);
        communicateBody.setRoundNum(this.roundNum);
    }



    private void updateRound(){
        Offer offer = lastCounterBody.getOffer();
        if(offer instanceof Proposal){
            Proposal proposal = (Proposal) offer;
            this.round.setProposal(proposal);
            //this.roundNum++;
            this.sessionRoundNum++;
        }else if(offer instanceof Response){
            Response response = (Response) offer;
            this.round.addResponse(response);
            if(this.round.getResponseAmount() == this.round.getAllisAmount()){
                this.roundEnd = true;
                this.roundNum++;
                if(this.round.getAgreeAmounts() == this.round.getAllisAmount() || this.sessionRoundNum >= this.getMaxRound()){
                    /**
                     * session 结束
                     * 下一轮游戏
                     * 没有new Round()
                     */
                    sessionEnd = true;
                    if(this.round.getAgreeAmounts() == this.round.getAllisAmount()){
                        this.isAgree = true;
                    }else{
                        this.isAgree = false;
                    }
                    //this.sessionNum ++;
                    if(this.sessionNum >= this.sessionAmounts){
                        this.gameEnd = true;
                    }
                }else{

                }
            }
        }
    }

    /*public boolean isSessionEnd(){
        if(this.round.getResponseAmount() != this.round.getAllisAmount()){
            return false;
        }else{
            if(this.round.getAgreeAmounts() != this.round.getAllisAmount()){
                return false;
            }else{
                return true;
            }
        }
    }*/

    @Override
    public boolean isNegoEnd() {
        return false;
    }
}
