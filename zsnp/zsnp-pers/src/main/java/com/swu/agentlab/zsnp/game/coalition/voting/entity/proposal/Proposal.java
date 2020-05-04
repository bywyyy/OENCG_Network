package com.swu.agentlab.zsnp.game.coalition.voting.entity.proposal;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class Proposal {

    private String id;

    private String roomId;

    private String playerId;

    private int sessionNum;

    private int roundNum;

    private String coalitionId;

    private String time;

    public Proposal getProposal(){
        return this;
    }

}
