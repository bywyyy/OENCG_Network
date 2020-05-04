package com.swu.agentlab.zsnp.springboot.entity;

import lombok.Data;

@Data
public class BatchResultMessage {
    private int partyNum;
    private String roleName;
    private String playerName;
    private Double payOff;
}
