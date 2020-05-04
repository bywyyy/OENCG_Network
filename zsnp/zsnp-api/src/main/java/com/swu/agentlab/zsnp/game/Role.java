package com.swu.agentlab.zsnp.game;

import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.entity.communicator.Sendable;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.gui.player.Arena;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JJ.Wu
 */
@Data
public abstract class Role implements OfferHandler ,Serializable {

    private String roleName;

    private Arena arena;

    public abstract RoleInfo generateRoleInfo();

    public abstract void speak(boolean speakable);

}
