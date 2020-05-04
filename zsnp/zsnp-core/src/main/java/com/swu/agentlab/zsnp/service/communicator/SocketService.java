package com.swu.agentlab.zsnp.service.communicator;

import com.swu.agentlab.zsnp.entity.communicator.Communicator;
import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SocketService implements CommunicatorService {

    public MySocket createMySocket (String serverAddr, int serverPort) throws IOException{
        MySocket socket = new MySocket(serverAddr, serverPort);
        return socket;
    }

    public void closeSocket(MySocket socket) throws IOException {
        socket.close();
    }

}
