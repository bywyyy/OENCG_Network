package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class CommunicateBody extends Body implements Serializable{


        private String roomId;

        private int sessionNum;

        private int roundNum;

        private Offer offer;

        public CommunicateBody(String roomId,Offer offer){
            this.bodyType = BodyType.COMMUNICATE_BODY;
            this.roomId = roomId;
            this.offer = offer;
        }


}
