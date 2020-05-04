package com.swu.agentlab.zsnp.game.coalition.voting.game;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author JJ.Wu
 */
@Data
public class Round {

    private Proposal proposal;

    private int allisAmount;

    private int responseAmount;

    private Set<Response> responses;

    private int agreeAmounts;

    public Round(){
        this.responses = new HashSet();
    }

    public void setProposal(Proposal proposal){
        this.proposal = proposal;
        this.setAllisAmount(proposal.getAlliesz().size());
    }

    public synchronized void addResponse(Response response){
        this.responses.add(response);
        this.responseAmount++;
        if(response.isAgree()){
            agreeAmounts++;
        }
    }

}
