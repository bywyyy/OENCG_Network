package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import lombok.Data;

/**
 * 某个房间的属性发生变化
 * 如：房间人数发生变化、房间游戏状态发生变化
 * @author JJ.Wu
 */
@Data
public class RoomInfoChange extends Body {

    private RoomInfo roomInfo;

    public RoomInfoChange() {
        this.bodyType = BodyType.ROOMINFO_CHANGE;
    }

    public RoomInfoChange(RoomInfo roomInfo) {
        this.bodyType = BodyType.ROOMINFO_CHANGE;
        this.roomInfo = roomInfo;
    }

}
