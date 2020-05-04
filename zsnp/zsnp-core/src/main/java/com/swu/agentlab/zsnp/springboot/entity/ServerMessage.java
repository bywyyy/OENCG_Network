package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@AllArgsConstructor
@Data
public class ServerMessage {
    private String serverMessage;
    private String time;
    private String  type;
    public static String jsonStr(String serverMessage,String time,String type) {
        return JSON.toJSONString(new ServerMessage(serverMessage,time,type));
    }
}
