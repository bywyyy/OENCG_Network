package com.swu.agentlab.zsnp.game.coalition.voting.agent;

import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import lombok.Getter;

import java.util.Set;

/**
 * 游戏的信息
 */
@Getter
public class Game {

    /**
     * 玩家数量
     */
    private int amountOfPlayers;

    /**
     * 形成联盟的必要条件
     * （weight总和>=majority）
     */
    private int majority;

    /**
     * 最多进行多少轮，超过轮数，本次协商失败
     */
    private int maxRoundNum;

    /**
     * 效益衰减因子
     */
    private double discount;

    /**
     * 所有可能的联盟形式
     */
    private Set<Coalition> coalitions;

    public Game(int amountOfPlayers, int majority, int maxRoundNum, double discount, Set<Coalition> coalitions) {
        this.amountOfPlayers = amountOfPlayers;
        this.majority = majority;
        this.maxRoundNum = maxRoundNum;
        this.discount = discount;
        this.coalitions = coalitions;
    }

    public int getRewards(Set<Integer> nums){
        int rewards = 0;
        for(Coalition item: coalitions){
            if(item.getPartyNums().equals(nums)){
                rewards = item.getRewards();
                break;
            }
        }
        return rewards;
    }

}
