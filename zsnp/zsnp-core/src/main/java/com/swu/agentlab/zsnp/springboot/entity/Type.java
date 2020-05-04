package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Type {
    String type;
    public static String jsonStr(String type) {
        return JSON.toJSONString(new Type(type));
    }
}
