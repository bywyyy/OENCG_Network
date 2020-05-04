package com.swu.agentlab.zsnp.entity.message.body;

import lombok.Data;

/**
 * 创建新房间的请求，
 * 包含：房间名、描述、域的全类名、协议全类名、消息管理器全类名
 * @author JJ.Wu
 */
@Data
public class RoomCreate extends Body {

    private String name;

    private String description;

    /**
     * 域的全类名
     * e.g. com.swu.agentlab.zsnp.XxxDomain
     */
    private String domainClass;

    private String protocolClass;

    private String messageManagerClass;

    private int maxStage;

    private int maxRound;

    private String stagePath;

    public RoomCreate() {
        this.bodyType = BodyType.ROOM_CREATE;
    }

    public RoomCreate(String name, String description){
        this.bodyType = BodyType.ROOM_CREATE;
        this.name = name;
        this.description = description;
    }

    public RoomCreate(String name, String description, String domainClass, String protocolClass, String messageManagerClass,int maxStage,int maxRound,
                      String stagePath) {
        this.bodyType = BodyType.ROOM_CREATE;
        this.name = name;
        this.description = description;
        this.domainClass = domainClass;
        this.protocolClass = protocolClass;
        this.messageManagerClass = messageManagerClass;
        this.maxStage = maxStage;
        this.maxRound = maxRound;
        this.stagePath = stagePath;
    }
}
