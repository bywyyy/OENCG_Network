package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class SingleRoomMessage {
    private RoomInfo roomInfo;
    private String type;
    public static String jsonStr(RoomInfo roomInfo, String type) {
        return JSON.toJSONString(new SingleRoomMessage(roomInfo,type));
    }
}
