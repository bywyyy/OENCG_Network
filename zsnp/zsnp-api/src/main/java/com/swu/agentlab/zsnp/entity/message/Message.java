package com.swu.agentlab.zsnp.entity.message;

import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.entity.communicator.Sendable;
import com.swu.agentlab.zsnp.entity.message.body.Body;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 *
 */
@Data
public class Message implements Serializable {


    private String fromId;

    private String fromName;

    private Set<String> toIds;

    private Body body;

    private String time;

    public Message(){}

    public Message(String time){
        this.time = time;
    };

    public Message(String fromId, String fromName, Set<String> toIds, Body body, String time) {
        this.fromId = fromId;
        this.fromName = fromName;
        this.toIds = toIds;
        this.body = body;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + fromName +
                ", body=" + body +
                ", time='" + time + '\'' +
                '}';
    }


}
