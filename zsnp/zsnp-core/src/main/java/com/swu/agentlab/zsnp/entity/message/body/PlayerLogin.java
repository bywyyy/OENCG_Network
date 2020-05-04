package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.Role;
import lombok.Data;

/**
 * 玩家简明信息，附带消息
 * @author JJ.Wu
 */
@Data
public class PlayerLogin extends Body {

    private PlayerInfo playerInfo;

    private String cont;

    public PlayerLogin(PlayerInfo playerInfo, String cont) {
        this.bodyType = BodyType.PLAYER_LOGIN;
        this.playerInfo = playerInfo;
        this.cont = cont;
    }
}
