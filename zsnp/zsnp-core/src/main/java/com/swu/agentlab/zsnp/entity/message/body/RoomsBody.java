package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import lombok.Data;

import java.util.Set;

/**
 * @author JJ.Wu
 */
@Data
public class RoomsBody extends Body {

    private Set<RoomInfo> roomInfos;

    public RoomsBody(){
        this.bodyType = BodyType.ROOMS_BODY;
    }

    public RoomsBody(Set<RoomInfo> roomInfos) {
        this.bodyType = BodyType.ROOMS_BODY;
        this.roomInfos = roomInfos;
    }
}
