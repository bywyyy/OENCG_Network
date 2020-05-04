package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@Data
public class AveragePayoffMessage {
    List<AveragePayoff> averagePayoff;
    String type;
    public static String jsonStr(List<AveragePayoff> averagePayoff,String type) {
        return JSON.toJSONString(new AveragePayoffMessage(averagePayoff,type));
    }
}
