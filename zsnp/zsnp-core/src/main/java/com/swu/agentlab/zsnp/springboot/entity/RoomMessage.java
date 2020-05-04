package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
@AllArgsConstructor
@Data
public class RoomMessage {
    private Set<RoomInfo> roomInfos;
    private String type;
    public static String jsonStr(Set<RoomInfo> roomInfos,String type) {
        return JSON.toJSONString(new RoomMessage(roomInfos,type));
    }
}
