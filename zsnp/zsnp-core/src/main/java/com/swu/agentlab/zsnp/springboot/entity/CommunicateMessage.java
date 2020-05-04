package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommunicateMessage {
    private String communicateLanguage;
    private String playerName;
    private String roleName;
    private int playerNum;
    private String time;
    private String type;
    private String emotion;
    public static String jsonStr(String communicatteLanguage,String playerName,String roleName,int playerNum,String time,String type,String emotion) {
        return JSON.toJSONString(new CommunicateMessage(communicatteLanguage,playerName,roleName,playerNum,time,type,emotion));
    }
}
