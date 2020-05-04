package com.swu.agentlab.zsnp.service.admin;

import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import com.swu.agentlab.zsnp.entity.player.admin.RemoteAdmin;
import com.swu.agentlab.zsnp.service.communicator.SocketService;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class AdminService {

    public static RemoteAdmin createRemoteAdmin(String address, int port) throws IOException {
        SocketService socketService = new SocketService();
        MySocket mySocket = socketService.createMySocket(address, port);
        RemoteAdmin remoteAdmin = new RemoteAdmin(mySocket, address, port);
        mySocket.setReceiver(remoteAdmin);
        new Thread(()->{
            mySocket.connect();
        }).start();
        return remoteAdmin;
    }

    public static void main(String[] args) {
        try {
            AdminService.createRemoteAdmin("172.18.16.135", 1234);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
