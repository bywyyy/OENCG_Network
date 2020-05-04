package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.body.GameBegin;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class SessionBegin extends Offer {

    private int sessionNum;

    private String hint;

    public SessionBegin(){}

    public SessionBegin(int sessionNum, String hint) {
        this.sessionNum = sessionNum;
        this.hint = hint;
    }

}
