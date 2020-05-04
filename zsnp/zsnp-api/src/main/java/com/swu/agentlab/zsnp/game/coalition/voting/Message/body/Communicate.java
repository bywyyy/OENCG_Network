package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class Communicate extends Offer implements Serializable {

    private int sessionRoundNum;            //当前场次的轮数

    private PlayerInfo playerInfo;

    private String communicateFree;

    private String communicateType;

    private String emotion;

    public Communicate(){
    }

    public Communicate(PlayerInfo playerInfo,String communicateFree,String communicateType) {
        this.playerInfo = playerInfo;
        this.communicateFree = communicateFree;
        this.communicateType = communicateType;
    }
}
