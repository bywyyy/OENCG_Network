package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Data
public class SessionResultMessage {
   Set<ResultRewardMessage> resultRewardMessages= new HashSet<>();
   String type;
   String language;
   int partyNum;
    public static String jsonStr(Set<ResultRewardMessage> resultRewardMessages,String type,String language,int partyNum) {
        return JSON.toJSONString(new SessionResultMessage(resultRewardMessages, type, language,partyNum));
    }
}
