package com.swu.agentlab.zsnp.entity.player;

import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.game.RoleInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * id, name, role
 * @author JJ.Wu
 */
@Data
public class PlayerInfo implements Serializable {

    private String id;

    private String name;

    private String description;

    private RoleInfo roleInfo;

    public PlayerInfo(String id, String name, String description, RoleInfo roleInfo){
        this.id = id;
        this.name = name;
        this.description = description;
        this.roleInfo = roleInfo;
    }


    /**
     * 根据玩家的Id判断是否为同一个玩家
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        return o instanceof PlayerInfo? ((PlayerInfo) o).getId().equals(this.getId()):false;
    }

    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }

}
