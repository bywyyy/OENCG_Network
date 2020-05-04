package com.swu.agentlab.zsnp.entity.message.body.offer;

import lombok.Data;

import java.io.Serializable;

@Data
public class Offer implements Serializable {

    private int roundNum;

    private String time;

}
