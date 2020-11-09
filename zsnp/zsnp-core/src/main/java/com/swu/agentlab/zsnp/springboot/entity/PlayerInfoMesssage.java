package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class PlayerInfoMesssage {
    private String name;
    private String description;
    private String roleName;
    private int partyNum;
    private int resource;
    private double talent;
    private double talpublish;
    private String type;
    private int num;
    private int size;

    public static String jsonStr(String name, String description, String roleName, int partyNum, int resource, double talent, double talpublish, String type, int num, int size) {
        return JSON.toJSONString(new PlayerInfoMesssage(name, description, roleName, partyNum, resource, talent, talpublish, type, num, size));
    }

}
