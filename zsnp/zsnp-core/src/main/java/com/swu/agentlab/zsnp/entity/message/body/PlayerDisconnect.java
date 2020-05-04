package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import lombok.Data;

/**
 * 玩家与服务器失去连接，通常指玩家异常退出（房间状态非GAME_END)
 * @author JJ.Wu
 */
@Data
public class PlayerDisconnect extends Body {

    private PlayerInfo playerInfo;

    private String hint;

    private Statue statue;

    private int timeOut;

    public PlayerDisconnect() {
        this.bodyType = BodyType.PLAYER_DISCONNECT;
    }
}
