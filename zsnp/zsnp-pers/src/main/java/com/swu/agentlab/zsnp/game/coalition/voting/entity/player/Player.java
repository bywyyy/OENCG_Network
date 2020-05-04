package com.swu.agentlab.zsnp.game.coalition.voting.entity.player;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class Player {

    private String id;

    private String roomId;

    private String name;

    private String description;

    private int partyNum;

    public Player getPlayer(){
        return this;
    }

}
