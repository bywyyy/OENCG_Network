package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ButtonMessage {
    String proposal;
    String accept;
    String reject;
    String sendCommunicate;
    String type;
    public static String jsonStr(String proposal,String accept,String reject,String sendCommunicate,String type) {
        return JSON.toJSONString(new ButtonMessage(proposal,accept,reject,sendCommunicate,type));
    }
}
