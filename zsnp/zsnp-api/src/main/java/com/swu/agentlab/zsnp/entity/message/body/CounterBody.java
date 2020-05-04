package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 *回合消息体
 * room id, 下一回合发言的玩家，和Offer
 */
@Data
public class CounterBody extends Body implements Serializable {

    private String roomId;

    private int sessionNum;

    private int roundNum;

    private Set<String> nextSpeakers;

    private Offer offer;

    private String hint;

    public CounterBody(String roomId, Set<String> nextSpeakers, Offer offer) {
        this.bodyType = BodyType.COUNTER_BODY;
        this.roomId = roomId;
        this.nextSpeakers = nextSpeakers;
        this.offer = offer;
    }
}
