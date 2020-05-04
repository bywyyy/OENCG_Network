package com.swu.agentlab.zsnp.gui.admin;

import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.gui.Room.RoomSelectedListener;

public interface RoomHandler {

    /**
     *
     * @param name
     * @param description
     * @param domainClass
     * @param protocolClass
     * @param messageManagerClass
     */
    Room createRoom(String name, String description, String domainClass, String protocolClass,
                    String messageManagerClass,int maxStage,int maxRound,String stagePath);

    void deleteRoom(String roomId);

    void deleteRoomData(String roomId);

    void selectRoomByStatue(Statue statue);

}
