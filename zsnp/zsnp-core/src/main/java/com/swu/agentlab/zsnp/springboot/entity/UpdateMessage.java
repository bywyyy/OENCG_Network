package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateMessage {
    private int majority;
    private String type;


    public static String jsonStr(int majority,String type) {
        return JSON.toJSONString(new UpdateMessage(majority,type));
    }
}
