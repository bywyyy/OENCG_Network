package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class WebRoomVotingMessage {

    private int partyNum;

    private int resource;

    private double talent;

    private String roleName;

    private Statue statue;

    private String playerName;
    public static String jsonStr(int partyNum,int resource,double talent,String roleName,Statue statue,String playerName) {
        return JSON.toJSONString(new WebRoomVotingMessage(partyNum,resource,talent,roleName,statue,playerName));
    }
}
