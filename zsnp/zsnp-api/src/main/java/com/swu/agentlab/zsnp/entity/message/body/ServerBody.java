package com.swu.agentlab.zsnp.entity.message.body;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class ServerBody extends Body {

    private String cont;

    public ServerBody(String cont){
        this.bodyType = BodyType.SERVER_BODY;
        this.cont = cont;
    }

}
