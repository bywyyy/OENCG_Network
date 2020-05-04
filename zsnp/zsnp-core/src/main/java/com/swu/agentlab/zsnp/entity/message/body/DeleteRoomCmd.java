package com.swu.agentlab.zsnp.entity.message.body;

import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class DeleteRoomCmd extends Body {

    private String roomId;

    public DeleteRoomCmd(String roomId) {
        this.bodyType = BodyType.DELETE_ROOM_CMD;
        this.roomId = roomId;
    }
}
