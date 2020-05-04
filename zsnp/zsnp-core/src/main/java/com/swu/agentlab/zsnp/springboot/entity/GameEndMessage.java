package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GameEndMessage {
    List<ResultPayoff> resultPayoffs;
    String cont;
    String type;
    public static String jsonStr(List<ResultPayoff> resultPayoffs,String cont,String type) {
        return JSON.toJSONString(new GameEndMessage(resultPayoffs,cont, type));
    }
}
