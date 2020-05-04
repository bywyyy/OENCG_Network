package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.game.RoleInfo;
import lombok.Data;

/**
 * roomInfo, role, domain
 * @author JJ.Wu
 */
@Data
public class LoginResponse extends Body {

    private boolean admitted;

    private RoomInfo roomInfo;

    private Domain domain;

    private RoleInfo roleInfo;

    private String info;

    public LoginResponse(boolean admitted, RoomInfo roomInfo, Domain domain, RoleInfo roleInfo, String info) {
        this.setBodyType(BodyType.LOGIN_RESPONSE);
        this.admitted = admitted;
        this.roomInfo = roomInfo;
        this.domain = domain;
        this.roleInfo = roleInfo;
        this.info = info;
    }
}
