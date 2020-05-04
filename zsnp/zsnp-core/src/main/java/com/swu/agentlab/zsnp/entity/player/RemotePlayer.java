package com.swu.agentlab.zsnp.entity.player;

import com.swu.agentlab.zsnp.entity.communicator.Communicator;
import com.swu.agentlab.zsnp.entity.communicator.DisconnectionHandler;
import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.*;
import com.swu.agentlab.zsnp.entity.player.human.RemoteAgent;
import com.swu.agentlab.zsnp.entity.player.human.RemoteHuman;
import com.swu.agentlab.zsnp.entity.player.server.MyServer;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.gui.player.Launch;
import com.swu.agentlab.zsnp.gui.player.LaunchForm;
import lombok.Data;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 主要用户是服务器
 * 远程类玩家的父类
 * @author JJ.Wu
 */
@Data
public class RemotePlayer extends Player implements DisconnectionHandler {

    private static Logger log = Logger.getLogger(RemotePlayer.class);

    /**
     * 远程玩家的地址
     */
    private String addr;

    private String agentPath;

    private Class agentClass;

    /**
     * 远程玩家与服务器沟通的对象
     */
    protected Communicator communicator;

    private Receivable receiver;

    private RemoteHuman remoteHuman;

    private RemoteAgent remoteAgent;

//    private Receivable receiver2;
    public RemotePlayer(){}

    public RemotePlayer(String id, String name, String description, boolean connected, Communicator communicator, String addr, Receivable receiver, Launch launch, PlayerType type){
        super(id, name, description, connected, launch, type);
        this.setCommunicator(communicator);
        this.addr = addr;
        this.receiver = receiver;
    }

    /**
     * 服务器用的构造方法
     * @param id
     * @param communicator
     * @param receiver
     */
    public RemotePlayer(String id, Communicator communicator, String addr, Receivable receiver){
        this.setId(id);
        this.setCommunicator(communicator);
        this.addr = addr;
        this.receiver = receiver;
        this.communicator.setHandler(this);
    }


//    public RemotePlayer(Receivable receiver2){
//        this.receiver2 = receiver2;
//    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
        this.communicator.setReceiver(this);
    }

    @Override
    public synchronized void receiveMessage(Message message) {

        if(receiver!=null){
            receiver.receiveMessage(message);
//            receiver2.receiveMessage(message);
        }else{
            //log.info(message);
            Body cont = message.getBody();
            switch (cont.getBodyType()){
                case LAUNCH_RESPONSE:

                    LaunchResponse launchResponse = (LaunchResponse) cont;
                    this.setId(launchResponse.getPlayerId());
                    Set<RoomInfo> infos = launchResponse.getRoomInfos();
                    this.getLaunch().init(infos);
                    break;
                case ROOMINFO_CHANGE:
                    RoomInfoChange roomInfoChange = (RoomInfoChange) cont;
                    this.getLaunch().update(roomInfoChange.getRoomInfo());
                    break;
                case LOGIN_REQUEST:
                    /**
                     * 处理创建remoteHuman的信息
                     */
                    LoginRequest loginReq = (LoginRequest) cont;
                    this.setType(loginReq.getPlayerType());
                    this.agentPath = loginReq.getAgentPath();
                    this.agentClass = loginReq.getAgentClass();
                    loginReq.setAgentClass(null);
                    this.setName(loginReq.getName());
                    this.setDescription(loginReq.getDescription());
                    loginReq.setPlayerId(this.getId());
                    message.setFromId(this.getId());
                    message.setFromName(this.getName());
                    sendMessage(message);
                    //log.info(loginReq.getAgentPath());
                    break;
                case LOGIN_RESPONSE:
                    /**
                     * 发送login的信息，并根据信息创建不同的remoteHuman或者remoteAgent
                     */
                    LoginResponse loginRes = (LoginResponse)cont;
                    if(loginRes.isAdmitted()){
                        //被允许进入房间
                        this.getLaunch().hide();
                        switch (this.getType()){
                            case REMOTE_HUMAN:
                                remoteHuman = new RemoteHuman(this, loginRes.getRoomInfo(), loginRes.getDomain(), loginRes.getRoleInfo());
                                remoteHuman.receiveMessage(message);
                                break;

                            case REMOTE_AGENT:
                                remoteAgent = new RemoteAgent(this, loginRes.getRoomInfo(), loginRes.getDomain(), loginRes.getRoleInfo(), this.agentPath, this.agentClass);
                                remoteAgent.receiveMessage(message);
                                break;
                            default:

                                break;
                        }
                    }else{
                        //被拒绝进入房间
                        JOptionPane.showMessageDialog(((LaunchForm)this.getLaunch()).getFrame(), loginRes.getInfo(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case NEW_ROOM:
                    NewRoom newRoom = (NewRoom) cont;
                    this.getLaunch().roomCreate(newRoom.getRoomInfo());
                    break;
                case DELETE_ROOM_CMD:
                    DeleteRoomCmd deleteRoomCmd = (DeleteRoomCmd) cont;
                    String roomId = deleteRoomCmd.getRoomId();
                    this.getLaunch().removeRoom(roomId);
                    break;
                case SELECT_ROOM_CMD:
                    sendMessage(message);
                    break;
                case ROOMS_BODY:
                    RoomsBody roomsBody = (RoomsBody) cont;
                    Set<RoomInfo> roomInfos = roomsBody.getRoomInfos();
                    this.getLaunch().update(roomInfos);
                    break;
                case SERVER_BODY:
                    break;
                default:

                    break;
            }
        }
    }

    @Override
    public void sendMessage(Message message) {
        communicator.sendMessage(message);
    }

    public void connect(){
        communicator.connect();
    }

    @Override
    public void handleDisconnection() {
        /**
         * 玩家处理失去连接
         * 如果handle == null 表示服务器不处理失去连接事件（如玩家还未登陆进入任何房间，就不需要进行该事件的处理）
         */
        //log.info("handleDisconnection()");
        if(exitHandler != null){
            exitHandler.handleExit(this);
        }
    }
}
