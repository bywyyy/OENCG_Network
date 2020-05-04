package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import lombok.Data;

import java.util.Set;

/**
 *
 * @author JJ.Wu
 */
@Data
public class LaunchResponse extends Body {

    private String playerId;

    private Set<RoomInfo> roomInfos;

    public LaunchResponse(String playerId, Set<RoomInfo> roomInfos){
        this.bodyType = BodyType.LAUNCH_RESPONSE;
        this.playerId = playerId;
        this.roomInfos = roomInfos;
    }

}
