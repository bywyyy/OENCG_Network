package com.swu.agentlab.zsnp.game.coalition.voting;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 联盟结构
 * @author JJ.Wu
 */
@Data
public class Coalition implements Serializable {

    /**
     * 联盟中玩家的编号集合
     */
    private Set<Integer> partyNums;

    /**
     * 联盟中所有玩家的资源（weight）总和
     */
    private int resources;

    /**
     * 联盟的总资金（funds）
     * 盟友被分配的资金的总和小于或者等于联盟的总资金
     */
    private int rewards;

    private int rewardPublish;

    public Coalition(){
        this.partyNums = new HashSet<>();
    }

    public Coalition(Set<Integer> partyNums, int resources, int rewards,int rewardPublish) {
        this.partyNums = partyNums;
        this.resources = resources;
        this.rewards = rewards;
        this.rewardPublish = rewardPublish;
    }
}
