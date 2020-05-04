package com.swu.agentlab.zsnp.entity.protocol;

import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;

import java.util.Set;

/**
 * @author JJ.Wu
 */
public abstract class Protocol {


    protected CounterBody lastCounterBody;

    protected CommunicateBody lastCommunicateBody;
    /**
     * 获得下一轮说话的player
     * @return 下一轮说话的player(或id)
     */
    public abstract Set nextSpeakers();

    /**
     * 判断协商是否结束
     * @return true协商结束，false协商未结束
     */
    public abstract boolean isNegoEnd();

    public void receiveCounterBody(CounterBody counterBody){
        this.lastCounterBody = counterBody;
    }

    public void receiveCommunicateBody(CommunicateBody communicateBody){this.lastCommunicateBody = communicateBody;}
}
