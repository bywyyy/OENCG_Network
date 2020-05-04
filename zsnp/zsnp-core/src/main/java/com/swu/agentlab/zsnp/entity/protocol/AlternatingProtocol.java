package com.swu.agentlab.zsnp.entity.protocol;

import java.util.Set;

public class AlternatingProtocol extends Protocol {
    @Override
    public Set nextSpeakers() {
        return null;
    }

    @Override
    public boolean isNegoEnd() {
        return false;
    }
}
