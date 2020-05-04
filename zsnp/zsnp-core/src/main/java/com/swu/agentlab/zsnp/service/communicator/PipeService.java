package com.swu.agentlab.zsnp.service.communicator;

import com.swu.agentlab.zsnp.entity.communicator.pipeline.Pipe;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.PlayerLaunch;
import com.swu.agentlab.zsnp.util.Tuples2;

public class PipeService implements CommunicatorService {

    public static Tuples2<Pipe> createPipePair(){
        Pipe pipe1 = new Pipe();
        Pipe pipe2 = new Pipe();
        pipe1.connect(pipe2.getPos(), pipe2.getPis());

        //pipe2.connect(pipe1.getPos(), pipe1.getPis());
        Tuples2 tuples2 = new Tuples2<>(pipe1, pipe2);
        return tuples2;
    }

}
