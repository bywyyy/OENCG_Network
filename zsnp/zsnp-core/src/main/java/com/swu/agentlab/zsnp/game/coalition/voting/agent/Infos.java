package com.swu.agentlab.zsnp.game.coalition.voting.agent;

import lombok.Getter;

/**
 * 初始化时传入的信息
 * 包括自己的信息
 *public void init(Infos infos)
 */
@Getter
public class Infos {

    /**
     * 玩家的编号
     */
    private int num;

    /**
     * 玩家的权重
     */
    private int weight;

    /**
     * 开销(Cost)
     */
    private double talent;

    public Infos(int num, int weight, double talent) {
        this.num = num;
        this.weight = weight;
        this.talent = talent;
    }

}
