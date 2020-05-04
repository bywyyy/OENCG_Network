package com.swu.agentlab.zsnp.controller.room;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import com.swu.agentlab.zsnp.entity.room.*;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.impl.RoomControllerImpl;
import com.swu.agentlab.zsnp.service.room.RoomService;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * 房间的控制类
 * 每个服务器持有一个
 * @author JJ.Wu
 */
public class RoomController extends BaseRoomController {

    private final Logger log = Logger.getLogger(RoomController.class);

    private com.swu.agentlab.zsnp.game.coalition.voting.controller.RoomController roomController;

    public RoomController(){
        super();
        roomService = new RoomService();
        //roomController = new RoomControllerImpl();
    }

    @Override
    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    @Override
    public Room createRoom(String name, String description, Domain domain, Protocol protocol, BaseMessageManager messageManager,int maxStage,int maxRound,String stagePath) {
        Room room = roomService.createRoom(name, description, domain, protocol, messageManager,maxStage,maxRound,stagePath);
        rooms.add(room);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //roomController.add(room.generateInfo());
            }
        }).start();
        return room;
    }

    @Override
    public synchronized String addPlayer(Player player, String roomId){


        Room room = rooms.get(roomId);

        if(room.getStatue()==Statue.PRE_GAME){
            Set<String> names = roomService.getPlayerNames(room);
            if(names.contains(player.getName())){
                return "玩家名称在房间内已存在, 请重新输入.";
            }
            roomService.addPlayer(room, player);
            player.setExitHandler(this);
            return "";
        }else{
            return "房间正在游戏中, 请重新选择房间.";
        }
    }

    @Override
    public Set<RoomInfo> getAllRoomInfo(){
        Set<RoomInfo> roomInfos = new HashSet<>();
        for(Room item: rooms){
            roomInfos.add(roomService.generateRoomInfo(item));
        }
        return roomInfos;
    }

    @Override
    public void receiveCounterBody(String roomId, CounterBody counterBody) {
        Room room = rooms.get(roomId);
        roomService.receiveCounterBody(room, counterBody);
    }
    @Override

    public void receiveCommunicateBody(String roomId, CommunicateBody communicateBody) {
        Room room = rooms.get(roomId);
        roomService.receiveCommunicateBody(room,communicateBody);
    }
    @Override
    public void handleExit(Player player) {
        Room room = getRoomByPlayerId(player.getId());
        roomService.playerExit(room, player);
    }

    private Room getRoomByPlayerId(String playerId){
        Room room = null;
        for(Room item: rooms){
            if(item.getPlayerIds().contains(playerId)){
                room =  item;
            }
        }
        return room;
    }

    @Override
    public void deleteRoom(String roomId) {
        Room room = rooms.get(roomId);
        rooms.remove(room);
        room = null;
    }

    @Override
    public Set<RoomInfo> selectRoomsByName(String roomName) {
        return rooms.getRoomsWithName(roomName).generateRoomInfos();
    }

    @Override
    public Set<RoomInfo> selectRoomsByStatue(Statue statue) {
        return rooms.getRoomsWithStatue(statue).generateRoomInfos();
    }

    @Override
    public Set<RoomInfo> selectRoomsByNameStatue(String roomName, Statue statue) {
        if(roomName == null){
            return selectRoomsByStatue(statue);
        }
        if(statue == null){
            return  selectRoomsByStatue(Statue.ALL);
        }
        return rooms.getRoomsWithStatue(statue).getRoomsWithName(roomName).generateRoomInfos();
    }
}
