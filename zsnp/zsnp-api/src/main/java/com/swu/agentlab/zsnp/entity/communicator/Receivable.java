package com.swu.agentlab.zsnp.entity.communicator;

import com.swu.agentlab.zsnp.entity.message.Message;

public interface Receivable {

    abstract void receiveMessage(Message message);

}
