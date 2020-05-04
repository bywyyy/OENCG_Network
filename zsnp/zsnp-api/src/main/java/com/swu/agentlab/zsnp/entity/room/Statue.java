package com.swu.agentlab.zsnp.entity.room;

/**
 * 房间当前的状态
 * @author JJ.Wu
 * @date 2017/11/26
 */
public enum Statue {

    ALL,

    /**
     * 游戏开始之前，房间玩家人数不够，等待玩家进入，允许任意玩家进入
     */
    PRE_GAME,
    /**
     * 游戏正在进行
     */
    ON_GAME,
    /**
     *游戏暂停中，（如等待掉线玩家重新连接）
     */
    GAME_PAUSE,
    /**
     * 游戏结束
     */
    GAME_END,

}
