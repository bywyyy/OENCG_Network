package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class XMLMessage {
    List<CoalitionXML> coalitionXMLS;
    Set<PartyInfo> parties;
    int majority;
    String type;

    public static String jsonStr(List<CoalitionXML> coalitionXMLS, Set<PartyInfo> parties,int majority,String type) {
        return JSON.toJSONString(new XMLMessage(coalitionXMLS,parties,majority,type));
    }
}
