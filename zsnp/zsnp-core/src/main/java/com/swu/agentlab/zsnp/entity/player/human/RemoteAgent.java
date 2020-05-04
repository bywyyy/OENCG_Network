package com.swu.agentlab.zsnp.entity.player.human;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.player.Agent;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.RoleInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.impl.GentleAgent;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.impl.RandomAgent;

public class RemoteAgent extends RemoteHuman {

    private Agent agent;

    public RemoteAgent(RemotePlayer remotePlayer, RoomInfo roomInfo, Domain domain, RoleInfo roleInfo, String agentPath, Class agentClass) {
        super(remotePlayer, roomInfo, domain, roleInfo);
        this.setHumanOrAgent("agent");
        if(domain instanceof VotingDomain){
            try {
                //agent = (Agent) Class.forName(agentPath).newInstance();
                //agent = new RandomAgent();
                agent = (Agent) agentClass.newInstance();
                ((VotingAgent)agent).init(roomInfo.getId(), this, (VotingDomain) domain, this.generatePlayerInfo(), roomInfo.getPlayerInfos());
                this.getRole().getArena().setAgent(agent);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
