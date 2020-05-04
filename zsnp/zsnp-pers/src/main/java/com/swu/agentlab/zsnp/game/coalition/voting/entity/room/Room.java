package com.swu.agentlab.zsnp.game.coalition.voting.entity.room;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class Room {

    private String id;

    private String name;

    private String description;

    private String domain_name;

    private String protocol_class;

    public Room getRoom(){
        return this;
    }

}
