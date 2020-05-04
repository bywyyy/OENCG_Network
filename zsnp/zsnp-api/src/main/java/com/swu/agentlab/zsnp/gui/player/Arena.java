package com.swu.agentlab.zsnp.gui.player;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.message.result.Result;
import com.swu.agentlab.zsnp.entity.player.Agent;
import com.swu.agentlab.zsnp.entity.player.PlayerExitHandler;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

/**
 * 游戏主界面
 * 用户Human玩家
 * @author JJ.Wu
 */
public abstract class Arena implements PlayerExitHandler {

    @Getter@Setter
    protected Agent agent;


    @Getter
    protected JFrame frame;

    /**
     * 初始化游戏主界面
     * @param domain 游戏的域
     */
    public abstract void init(PlayerInfo playerInfo, RoomInfo roomInfo, Domain domain);

    /**
     *显示
     */
    public abstract void show();

    /**
     *隐藏
     */
    public abstract void hide();

    /**
     * 关闭
     */
    public abstract void close();

    /**
     * 接收服务器消息
     */
    public abstract void printServerMessage(String time, String cont);

    /**
     * 打印Counter Offer
     * @param offer
     */
    public abstract void printOffer(Offer offer, String time);

    /**
     * 更具房间类玩家的信息，更新界面
     * 一般用于新的玩家进入游戏，需要更新游戏界面
     * @param playerInfo playerId, playerNmae, plyaerRole
     */
    public abstract void update(PlayerInfo playerInfo);

    /**
     * 当房间状态更新时，可调用该方法更新界面显示
     * @param roomStatue
     */
    public abstract void update(Statue roomStatue);

    public abstract void printGameResult(Result gameResult);

}
