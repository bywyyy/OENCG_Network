package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class OfferMessage {
    Offer offer;

    String time;

    String type;

    int sessionNum;

    public static String jsonStr(Offer offer,String time,String type,int sessionNum) {
        return JSON.toJSONString(new OfferMessage(offer,time,type,sessionNum));
    }
}
