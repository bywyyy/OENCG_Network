package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 玩家player info，是否同意proposal，评论comment
 * @author JJ.Wu
 */
@Data
public class Response extends Offer implements Serializable {

    private PlayerInfo playerInfo;

    private boolean agree;

    private Comment comment;

    public Response() {
    }

    /**
     * @param playerInfo
     * @param agree
     * @param comment
     */
    public Response(PlayerInfo playerInfo, boolean agree, Comment comment) {
        this.playerInfo = playerInfo;
        this.agree = agree;
        this.comment = comment;
    }
}
