package com.swu.agentlab.zsnp.gui.admin;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;

import java.util.Set;

public abstract class BaseAdminForm {

    public abstract void init(Set<RoomInfo> roomInfos);

    public abstract void appendRoom(RoomInfo roomInfo);

    public abstract void updateAllRooms(Set<RoomInfo> roomInfos);

    public abstract void removeRoom(String roomId);

    public abstract void updateRoomInfo(RoomInfo roomInfo);

//    这部分是移植功能到网页端的函数
    public abstract void adminCreatRoom();

    public abstract void adminDeleteRoom(String roomId);

    public abstract void adminDeleteRoomData(String roomId);

    public abstract void adminLaunchClient();
}
