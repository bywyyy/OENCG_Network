package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.player.PlayerType;
import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class LoginRequest extends Body {

    private String playerId;

    private String name;

    private String description;

    private PlayerType playerType;

    private String agentPath;

    private Class agentClass;

    private String roomId;

    public LoginRequest(String playerId, String name, String description, PlayerType playerType, String agentPath, String roomId) {
        this.bodyType = BodyType.LOGIN_REQUEST;
        this.playerType = playerType;
        this.playerId = playerId;
        this.name = name;
        this.description = description;
        this.agentPath = agentPath;
        this.roomId = roomId;
    }
}
