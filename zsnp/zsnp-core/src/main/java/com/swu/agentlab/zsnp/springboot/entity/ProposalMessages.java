package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@Data
public class ProposalMessages {

    Set<ProposalMessage> offermessages = new HashSet<>();
    String type;
    String proposalName;
    String  proposalCompanyName;
    int roundNum;
    int SessionRoundNum;
    int proposalNum;
    String coalition;
    String emotion;
    int amountOfPlayer;
    public static String jsonStr(Set<ProposalMessage> offermessages,String type,String proposalName,
                                 String  proposalCompanyName,int roundNum,int sessionNum,int proposalNum,
                                 String coalition,String emotion,int amountOfPlayer) {
        return JSON.toJSONString(new ProposalMessages(offermessages,type,proposalName,proposalCompanyName,
                roundNum,sessionNum,proposalNum,coalition,emotion,amountOfPlayer));
    }
}
