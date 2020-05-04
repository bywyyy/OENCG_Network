package com.swu.agentlab.zsnp.entity.player.server;

import com.swu.agentlab.zsnp.controller.room.BaseRoomController;
import com.swu.agentlab.zsnp.entity.player.Player;
import lombok.Data;

@Data
public abstract class Server extends Player {

    protected BaseRoomController roomController;

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始接收本地玩家连接......");
                acceptLocalPlayer();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始接收远程玩家连接......");
                acceptRemotePlayer();
            }
        }).start();
    }

    public abstract void acceptRemotePlayer();

    public abstract void acceptLocalPlayer();
}
