package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseMessage {
    String playerName;
    String roleName;
    String language;
    String type;
    String specificType;
    String emotion;
    int playerNum;
    public static String jsonStr(String playerName, String roleName, String language,
                                 String type,String specificType,String emotion,int playerNum) {
        return JSON.toJSONString(new ResponseMessage(playerName,roleName,language,type,
                specificType,emotion,playerNum));
    }
}
