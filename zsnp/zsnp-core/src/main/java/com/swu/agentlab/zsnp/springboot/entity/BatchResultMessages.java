package com.swu.agentlab.zsnp.springboot.entity;


import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BatchResultMessages {
    private List<BatchResultMessage> batchResultMessages;

    public static String jsonStr(List<BatchResultMessage> batchResultMessages) {
        return JSON.toJSONString(new BatchResultMessages(batchResultMessages));
    }
}
