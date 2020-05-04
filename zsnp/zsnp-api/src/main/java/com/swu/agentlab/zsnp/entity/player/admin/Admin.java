package com.swu.agentlab.zsnp.entity.player.admin;

import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.gui.admin.BaseAdminForm;
import lombok.Data;

@Data
public abstract class Admin {

    /**
     * 管理服务器的主界面
     */
    protected BaseAdminForm adminForm;

}
