package com.swu.agentlab.zsnp.service.room;

import com.swu.agentlab.zsnp.entity.player.Player;

public interface PlayerDisconnectionHandler {

    /**
     * 处理某个玩家失去连接的事件
     * @param player 失去连接的玩家
     */
    void handleDisconnection(Player player);

}
