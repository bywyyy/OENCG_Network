package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QueryStatue {
    private String statue;
    public static String jsonStr(String statue) {
        return JSON.toJSONString(new QueryStatue(statue));
    }
}
