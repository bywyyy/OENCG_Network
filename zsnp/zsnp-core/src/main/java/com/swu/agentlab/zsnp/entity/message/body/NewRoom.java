package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import lombok.Data;

/**
 * 新的房间被创建
 * 包括：新房间的信息，RoomInfo
 * @author JJ.Wu
 */
@Data
public class NewRoom extends Body{

    private RoomInfo roomInfo;

    public NewRoom(RoomInfo roomInfo){
        this.bodyType = BodyType.NEW_ROOM;
        this.roomInfo = roomInfo;
    }

}
