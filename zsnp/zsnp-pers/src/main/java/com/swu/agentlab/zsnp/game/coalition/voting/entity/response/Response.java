package com.swu.agentlab.zsnp.game.coalition.voting.entity.response;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class Response {

    private String id;

    private String roomId;

    private String playerId;

    private int sessionNum;

    private int roundNum;

    private boolean agree;

    private String time;

    public Response getResponse(){
        return this;
    }

}
