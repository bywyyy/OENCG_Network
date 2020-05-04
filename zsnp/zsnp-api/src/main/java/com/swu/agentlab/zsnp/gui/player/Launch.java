package com.swu.agentlab.zsnp.gui.player;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;

import java.awt.*;
import java.util.Set;

/**
 * 玩家填写player信息，选择房间的界面
 * @author JJ.Wu
 */
public abstract class Launch {

    /**
     * 初始化
     */
    public abstract  void init(Set<RoomInfo> roomInfos);

    /**
     * 显示
     */
    public abstract void show();

    /**
     * 隐藏
     */
    public abstract void hide();

    /**
     * 关闭
     */
    public abstract void close();

    /**
     * 根据某个房间的信息，更新界面房间显示信息
     * @param roomInfo
     */
    public abstract void update(RoomInfo roomInfo);

    /**
     * 新房间被创建，界面上加入新房间的信息
     * @param roomInfo
     */
    public abstract void roomCreate(RoomInfo roomInfo);

    /**
     *
     * @param roomId
     */
    public abstract void removeRoom(String roomId);

    /**
     * 根据房间集合更新启动面板内的房间信息
     * @param roomInfos
     */
    public abstract  void update(Set<RoomInfo> roomInfos);

    /**
     * button ok 的移植
     */

    public abstract  void ButtonOk(String playerName,String desc,String roomId,String playerType);
}
