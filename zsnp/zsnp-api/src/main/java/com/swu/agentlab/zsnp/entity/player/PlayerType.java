package com.swu.agentlab.zsnp.entity.player;

/**
 * 玩家的类型，
 */
public enum PlayerType {

    /**
     * 该服务器本地Human玩家
     */
    LOCAL_HUMAN,
    /**
     * 该服务器本地Agent玩家
     */
    LOCAL_AGENT,
    /**
     * 远程Human玩家
     */
    REMOTE_HUMAN,
    /**
     * 远程Agent玩家
     */
    REMOTE_AGENT,

}
