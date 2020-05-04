package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.body.BodyType;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author JJ.Wu
 */
@Data
public class Proposal extends Offer implements Serializable {

    private int sessionRoundNum;            //当前场次的轮数

    private Map<PlayerInfo, Integer> proposerz;//玩家信息

    private Map<PlayerInfo, Toallyz> alliesz;

    public Proposal() {
    }

    public Proposal(Map<PlayerInfo, Integer> proposerz, Map<PlayerInfo, Toallyz> alliesz) {
        this.proposerz = proposerz;
        this.alliesz = alliesz;
    }


}
