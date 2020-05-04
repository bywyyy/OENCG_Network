package com.swu.agentlab.zsnp.entity.communicator;

import lombok.Setter;

public abstract class Communicator implements Receivable, Sendable {

    @Setter
    protected Receivable receiver;

    @Setter
    protected DisconnectionHandler handler;

    public abstract void connect();

}
