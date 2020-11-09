package com.swu.agentlab.zsnp.springboot.entity;

import lombok.Data;

import java.util.Set;
@Data
public class CoalitionXML {
    Set<Integer> partyNums;

    String corporationsText;

    int resources;

    int rewards;

    int rewardPublish;

}
