package com.swu.agentlab.zsnp.entity.room;

import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.ServerBody;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerSet;
import com.swu.agentlab.zsnp.util.TimeUtil;
import lombok.Setter;

public abstract class BaseMessageManager implements Receivable {

    @Setter
    protected Room room;

    public BaseMessageManager(){

    }

    protected void sendMessage(PlayerSet players, Message message){
        message.setTime(TimeUtil.getHms());
        for(Player item: players){
            synchronized (item){
                item.sendMessage(message);
            }
        }
    }

    /**
     * 通知刚刚进入放假的玩家，你已进入房间，并发送房间当前的信息
     * @param player
     */
    public abstract void sendLoginResponse(Player player);

    /**
     * 通知已在房间内的玩家，新的玩家进入房间
     * @param player 刚刚进入房间的玩家
     */
    public abstract void notifyLoginMessage(Player player);

    /**
     *通知所有玩家，游戏开始
     */
    public abstract void notifyGameStart();

    /**
     * 接收回合消息体
     */
    public abstract void receiveCounterBody(CounterBody counterBody);

    /**
     * 接收文字信息的模块
     * @param communicateBody
     */
    public abstract void receiveCommunicateBody(CommunicateBody communicateBody);





    /**
     * 通知在房间内的玩家，某个玩家退出了房间(正常退出)，房间状态为GAME_END
     * @param player 退出房间的玩家
     */
    public abstract void notifyLoginoutMessage(Player player);

    /**
     * 通知房间内的玩家，某个玩家已经离线(非正常退出)，房间状态不是
     * @param player
     */
    public abstract void notifyDisconnectMessage(Player player);

}
