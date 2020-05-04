package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.room.Statue;
import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class SelectRoomCmd extends Body {

    private String playerId;

    private Statue roomStatue;

    private String roomName;

    public SelectRoomCmd() {
        this.bodyType = BodyType.SELECT_ROOM_CMD;
    }

    public SelectRoomCmd(String playerId, Statue roomStatue) {
        this.bodyType = BodyType.SELECT_ROOM_CMD;
        this.playerId = playerId;
        this.roomStatue = roomStatue;
    }
}
