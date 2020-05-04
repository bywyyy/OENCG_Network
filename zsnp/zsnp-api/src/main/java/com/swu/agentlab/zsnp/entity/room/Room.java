package com.swu.agentlab.zsnp.entity.room;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerSet;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import lombok.Data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author JJ.Wu
 */
@Data
public class Room {

    private String id;

    private String name;

    private String description;

    /**
     * 玩家集合
     */
    private PlayerSet players;

    /**
     * 玩家id的列表
     * 按照玩家进入房间的顺序添加
     */
    private LinkedList<String> playerIds;

    /**
     * 当前房间内的玩家
     */
    private int amountOfPlayers;

    /**
     * 该房间的域
     */
    private Domain domain;

    /**
     * 游戏控制协议，控制游戏流程
     */
    private Protocol protocol;

    /**
     * 该房间当前的状态
     */
    private Statue statue;

    /**
     * 房间的消息管理器
     */
    private BaseMessageManager messageManager;

    public RoomInfo generateInfo(){
        Set playerInfos = new HashSet();
        for(Player item: players){
            playerInfos.add(item.generatePlayerInfo());
        }
        return new RoomInfo(this.getId(), this.getName(), this.getAmountOfPlayers(), playerInfos,this.getDomain().getAmountRoles(), this.getDomain().getName(), this.getDescription(), this.getStatue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Room room = (Room) o;

        return id != null ? id.equals(room.id) : room.id == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
