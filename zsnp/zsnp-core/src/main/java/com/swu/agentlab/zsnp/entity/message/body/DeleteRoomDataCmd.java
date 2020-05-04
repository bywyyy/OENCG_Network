package com.swu.agentlab.zsnp.entity.message.body;

public class DeleteRoomDataCmd extends Body {

    private String roomId;

    public DeleteRoomDataCmd(String roomId) {
        this.bodyType = BodyType.DELETE_ROOM_DATA_CMD;
        this.roomId = roomId;
    }
}
