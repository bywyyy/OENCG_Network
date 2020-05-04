package com.swu.agentlab.zsnp.entity.communicator;

import com.swu.agentlab.zsnp.entity.message.Message;

public interface Sendable {

    abstract void sendMessage(Message message);

}
