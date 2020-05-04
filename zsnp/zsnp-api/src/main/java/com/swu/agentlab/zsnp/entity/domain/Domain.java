package com.swu.agentlab.zsnp.entity.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JJ.Wu
 */
@Data
public class Domain implements Serializable {

    /**
     * 域id
     */
    private String id;

    private String name;

    private int amountRoles;

    private String description;

}
