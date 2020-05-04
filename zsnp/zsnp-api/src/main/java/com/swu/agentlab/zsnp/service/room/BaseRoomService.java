package com.swu.agentlab.zsnp.service.room;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerExitHandler;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import com.swu.agentlab.zsnp.entity.room.BaseMessageManager;
import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;

import java.util.Set;

/**
 * @author JJ.Wu
 */
public abstract class BaseRoomService {

    public abstract Room createRoom(String name, String description, Domain domain, Protocol protocol,
                                    BaseMessageManager messageManager,int maxStage,int maxRound,String stagePath);

    public abstract RoomInfo generateRoomInfo(Room room);

    /**
     * 添加一个玩家到该房间
     * synchronized(room)，房间同步锁，
     * 同一时刻只有一个玩家可以进入该房间
     * 1，更新房间人数amountOfPlayer
     * 2，更新房间当前的状态
     * @param room
     * @param player
     */
    public abstract Role addPlayer(Room room, Player player);

    /**
     * 更新房间状态
     * synchronized方法，同一时刻只有一个用户可以调用该方法
     * 同一时刻只有一个用户可以更新房间状态
     * @param room
     */
    public abstract void updateRoomStatue(Room room);

    public abstract void receiveCounterBody(Room room, CounterBody counterBody);

    public abstract void receiveCommunicateBody(Room room, CommunicateBody communicateBody);

    public abstract Set<String> getPlayerNames(Room room);

    public abstract void playerExit(Room room, Player player);

    public abstract void deleteRoom(Room room);

}
