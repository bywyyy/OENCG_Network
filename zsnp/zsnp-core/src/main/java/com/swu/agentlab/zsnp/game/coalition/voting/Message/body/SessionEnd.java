package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author JJ.Wu
 */
@Data
public class SessionEnd extends Offer {

    private boolean agree;

    private int sessionNum;

    private Map<PlayerInfo, Double> rewards;

    private String hint;

    public SessionEnd() {
        this.rewards = new HashMap<>();
    }
}
