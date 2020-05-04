package com.swu.agentlab.zsnp.entity.player.admin;

import com.swu.agentlab.zsnp.entity.communicator.Communicator;
import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.*;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.gui.admin.AdminForm;
import com.swu.agentlab.zsnp.gui.admin.RoomHandler;
import com.swu.agentlab.zsnp.gui.player.Launch;
import com.swu.agentlab.zsnp.service.player.RemotePlayerService;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.controller.WebAdminController;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;


public class RemoteAdmin extends Admin implements Receivable, RoomHandler, LaunchHandler {

    private final Logger log = Logger.getLogger(RemoteAdmin.class);

    private String id;
    private Message message1;
    public Communicator communicator;

    @Getter
    private Set<RoomInfo> roomInfos;

    private String serverAddr;

    private int serverPort;

    public RemotePlayer player;

    public Launch launch;

    @Setter
    private LaunchHandler launchHandler;
    @Setter
    private WebVotingArena webVotingArena;

    public RemoteAdmin(Communicator communicator, String serverAddr, int serverPort) {
        this.communicator = communicator;
        this.adminForm = new AdminForm(this, this);
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
    }
/*
* 这是移植的client中的按键函数,这是房间客户端的几个函数
* */
    public void createRoomButton(){
        this.adminForm.adminCreatRoom();
    }

    public void deleteRoomButton(String roomId){
        this.adminForm.adminDeleteRoom(roomId);
    }

    public void deleteRoomDataButton(String roomId){
        this.adminForm.adminDeleteRoomData(roomId);
    }

    public void okButton(){
        this.adminForm.adminLaunchClient();
    }
/*
* 这是移植的launch中的按键函数
* */
//    public void queRenButton(String playerName,String desc,String roomId,String playerType){
//        launch.ButtonOk(playerName,desc,roomId,playerType);
//    }




    @Override
    public void receiveMessage(Message message) {
//        this.message1 = message;
        Body body = message.getBody();
        //log.info(body);
        switch (body.getBodyType()){
            case LAUNCH_RESPONSE:
                LaunchResponse launchRes = (LaunchResponse) body;
                this.id = launchRes.getPlayerId();
                this.roomInfos = launchRes.getRoomInfos();
                this.adminForm.init(this.roomInfos);
                break;
            case NEW_ROOM:
                NewRoom newRoom = (NewRoom) body;
                RoomInfo roomInfo = newRoom.getRoomInfo();
                roomInfos.add(roomInfo);
                this.adminForm.appendRoom(roomInfo);
                if(webVotingArena != null) {
                    webVotingArena.sendGetRoomMessageToWeb("UpdateRoom");
                }
                break;
            case ROOMS_BODY:
                RoomsBody roomsBody = (RoomsBody) body;
                this.adminForm.updateAllRooms(roomsBody.getRoomInfos());
                break;
            case DELETE_ROOM_CMD:
                DeleteRoomCmd deleteRoomCmd = (DeleteRoomCmd) body;
                String roomId = deleteRoomCmd.getRoomId();
                this.adminForm.removeRoom(roomId);
                if(webVotingArena != null) {
                    webVotingArena.sendGetRoomMessageToWeb("UpdateRoom");
                }
                break;
            case ROOMINFO_CHANGE:
                RoomInfoChange roomInfoChange = (RoomInfoChange) body;
                this.adminForm.updateRoomInfo(roomInfoChange.getRoomInfo());
                break;
        }

    }

    public int getAmountOfRooms(){
        if(roomInfos == null){
            return 0;
        }
        return this.roomInfos.size();
    }

    @Override
    public Room createRoom(String name, String description, String domainClass, String protocolClass, String messageManagerClass,int maxStage,int maxRound,String stagePath) {
//        System.out.println("this is remotePlayer CreateRoomBridge ");
//        System.out.println(this.communicator);
//        System.out.println(this.serverAddr);
//        System.out.print(this.serverPort);
        RoomCreate roomCreate = new RoomCreate(name, description, domainClass, protocolClass, messageManagerClass,maxStage,maxRound,stagePath);
        communicator.sendMessage(new Message(null, null, null, roomCreate, null));
        return null;
    }

    @Override
    public void deleteRoom(String roomId) {
         DeleteRoomCmd deleteRoomCmd = new DeleteRoomCmd(roomId);
        communicator.sendMessage(new Message(null, null, null, deleteRoomCmd, null));
    }

    @Override
    public void deleteRoomData(String roomId) {
        DeleteRoomDataCmd deleteRoomDataCmd = new DeleteRoomDataCmd(roomId);
        communicator.sendMessage(new Message(null, null, null, deleteRoomDataCmd, null));
    }

    @Override
    public void selectRoomByStatue(Statue statue) {
        SelectRoomCmd selectRoomCmd = new SelectRoomCmd(this.id, statue);
        communicator.sendMessage(new Message(null, null, null, selectRoomCmd, null));
    }

    @Override
    public String launchClient() {
        String playerId=null;
        if(launchHandler == null){
            try {
                this.player = RemotePlayerService.createRemotePlayer(this.serverAddr, this.serverPort);
                //RemotePlayerService.createRemotePlayerPair();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            playerId =  launchHandler.launchClient();
        }
        return playerId;
    }
}
