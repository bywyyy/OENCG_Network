package com.swu.agentlab.zsnp.game.coalition.voting.entity.ally;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class Ally {

    private String id;

    private String coalitionId;

    private String playerId;

    private int partyNum;

    private int reward;

    private String comment;

    public Ally getAlly(){
        return this;
    }

}
