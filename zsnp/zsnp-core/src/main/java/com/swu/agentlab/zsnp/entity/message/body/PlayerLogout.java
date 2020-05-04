package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import lombok.Data;

/**
 * 玩家退出房间，通常指玩家正常退出房间（房间状态为GAME_END，即房间内的游戏已结束）
 * @author JJ.Wu
 */
@Data
public class PlayerLogout extends Body {

    private PlayerInfo playerInfo;

    private String hint;

    private Statue statue;

    public PlayerLogout() {
        this.setBodyType(BodyType.PLAYER_LOGOUT);
    }

    public PlayerLogout(PlayerInfo playerInfo, String hint, Statue statue) {
        this.setBodyType(BodyType.PLAYER_LOGOUT);
        this.playerInfo = playerInfo;
        this.hint = hint;
        this.statue = statue;
    }
}
