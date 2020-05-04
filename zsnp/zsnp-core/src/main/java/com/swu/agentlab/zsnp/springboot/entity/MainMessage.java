package com.swu.agentlab.zsnp.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Data
public class MainMessage {

    private String MyId;
    private String RoomId;
    private String Name1;
    private int Name1Value;
    private String Name2;
    private int Name2Value;
    private String Name3;
    private int Name3Value;
    private String Name4;
    private int Name4Value;
    private String Name5;
    private int Name5Value;
    private String Type;
    private String CommunicateFree;
    private String CommunicateType;
    private String Emotion;

    public static String jsonStr(String MyId, String RoomId, String Name1, int Name1Value,
                                 String Name2, int Name2Value, String Name3, int Name3Value,String Name4,
                                 int Name4Value,String Name5,int Name5Value,
                                 String Type, String CommunicateFree,String CommunicateType,String Emotion) {
        return JSON.toJSONString(new MainMessage(MyId, RoomId, Name1, Name1Value, Name2,
                Name2Value, Name3, Name3Value,Name4,Name4Value,Name5,Name5Value, Type, CommunicateFree,CommunicateType,Emotion));
    }
}
