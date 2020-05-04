package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class SessionResult extends Offer implements Serializable {

    private boolean agree;

    private Map<String, String> results;

    public SessionResult(Map<String, String> results) {
        this.results = results;
    }
}
