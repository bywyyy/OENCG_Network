package com.swu.agentlab.zsnp.controller.room;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerExitHandler;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import com.swu.agentlab.zsnp.entity.room.*;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.service.room.BaseRoomService;

import java.util.Set;

/**
 * 房间的控制类
 * 每个服务器持有一个
 * @author JJ.Wu
 */
public abstract class BaseRoomController implements PlayerExitHandler {

    protected RoomSet rooms;

    protected BaseRoomService roomService;

    public BaseRoomController(){
        rooms = new RoomSet();
    }

    /**
     * 根据roomId获取room
     * @param roomId
     * @return
     */
    public abstract Room getRoom(String roomId);

    /**
     * 添加一个player到特定的房间内
     * @param player
     */
    public abstract String addPlayer(Player player, String roomId);

    /**
     * 获取当前服务器中所有的房间信息快照
     * @return
     */
    public abstract Set<RoomInfo> getAllRoomInfo();

    public abstract Room createRoom(String name, String description, Domain domain, Protocol protocol,
                                    BaseMessageManager messageManager,int maxStage,int maxRound,String stagePath);

    /**
     * 当某个房间内收到游戏回合消息时，调用
     */
    public abstract void receiveCounterBody(String roomId, CounterBody counterBody);

    /**
     * 当房间收到文字交流信息时调用
     * @param roomId
     * @param communicateBody
     */
    public abstract void receiveCommunicateBody(String roomId, CommunicateBody communicateBody);


    public abstract void deleteRoom(String roomId);

    /**
     * 通过房间名称查找房间信息（模糊查询）
     * @param roomName 房间名;
     * @return 房间信息集合Set<RoomInfo>
     */
    public abstract Set<RoomInfo> selectRoomsByName(String roomName);

    /**
     * 根据房间的状态查找房间信息
     * @param statue 房间当前的游戏状态(PRE_GAME, ON_GAME, GAME_PAUSE, GAME_END)
     * @return 房间信息集合Set<RoomInfo>
     */
    public abstract Set<RoomInfo> selectRoomsByStatue(Statue statue);

    public abstract Set<RoomInfo> selectRoomsByNameStatue(String roomName, Statue statue);
}
