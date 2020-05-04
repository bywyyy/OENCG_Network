package com.swu.agentlab.zsnp.game.coalition.voting;

import com.swu.agentlab.zsnp.game.RoleInfo;
import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class PartyInfo extends RoleInfo {

    private int partyNum;

    private int resource;

    private double talent;

    public PartyInfo(String roleName, int partyNum, int resource, double talent) {
        this.setRoleName(roleName);
        this.partyNum = partyNum;
        this.resource = resource;
        this.talent = talent;
    }

}
