package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class BatchMessage {
    private String agentPath1;
    private String agentPath2;
    private String agentPath3;
    private int maxStage;
    private int maxRound;

    public static String jsonStr(String agentPath1,String agentPath2,String agentPath3,int maxStage,int maxRound) {
        return JSON.toJSONString(new BatchMessage(agentPath1,agentPath2,agentPath3,maxStage,maxRound));
    }
}
