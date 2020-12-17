package com.swu.agentlab.zsnp.entity.room;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 房间的快照，房间的信息
 * 可以在玩家和服务器之间传递
 * @author JJ.Wu
 */
@Data
public class RoomInfo implements Serializable {

    private String id;

    private String name;

    private int amountOfPlayers;

    private Set<PlayerInfo> playerInfos;

    private int amountOfRoles;

    private String domainName;

    private String description;

    private Statue statue;

    public RoomInfo(String id, String name, int amountOfPlayers, Set playerInfos, int maxAmount, String domainName, String description, Statue statue) {
        this.id = id;
        this.name = name;
        this.amountOfPlayers = amountOfPlayers;
        this.playerInfos = playerInfos;
        this.amountOfRoles = maxAmount;
        this.domainName = domainName;
        this.description = description;
        this.statue = statue;
    }
}
