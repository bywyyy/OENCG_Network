package com.swu.agentlab.zsnp.springboot.Myclass;

import com.swu.agentlab.zsnp.conf.BaseConfigBundle;
import com.swu.agentlab.zsnp.controller.room.RoomController;
import com.swu.agentlab.zsnp.entity.message.Message;

import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.player.admin.LocalAdmin;
import com.swu.agentlab.zsnp.entity.player.admin.RemoteAdmin;
import com.swu.agentlab.zsnp.entity.player.server.MyServer;
import com.swu.agentlab.zsnp.entity.player.server.Server;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.SessionResult;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingProtocol;
import com.swu.agentlab.zsnp.game.coalition.voting.room.MessageManager;
import com.swu.agentlab.zsnp.gui.admin.RoomHandler;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.gui.config.GameConfBundle;
import com.swu.agentlab.zsnp.gui.player.Launch;
import com.swu.agentlab.zsnp.service.admin.AdminService;

import com.swu.agentlab.zsnp.service.player.RemotePlayerService;
import com.swu.agentlab.zsnp.springboot.ZsnpApplication;
import com.swu.agentlab.zsnp.springboot.controller.MyWebSocket;
import com.swu.agentlab.zsnp.springboot.entity.OfferMessage;
import com.swu.agentlab.zsnp.springboot.entity.ResultRewardMessage;
import com.swu.agentlab.zsnp.springboot.entity.SessionResultMessage;
import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@Service
public class WebBridgeToClient {

   private LocalAdmin localAdmin;

    private RemotePlayer remotePlayer;

    private Map<String,RemotePlayer>  remotePlayerMap = new HashMap<String,RemotePlayer>();

    private Launch launch;

    private Server server;

    private RoomController roomController;

    private RemoteAdmin remoteAdmin;

    private Set<RoomInfo> roomInfos;

    private GUIBundle guiBundle;

    public WebBridgeToClient(){
        this.server = ZsnpApplication.server;
        this.remoteAdmin = ((MyServer) server).remoteAdmin;
        this.guiBundle = GUIBundle.getInstance("admin");
    }

    public void createRoom(String stagePath) {
        roomInfos = remoteAdmin.getRoomInfos();
        remoteAdmin.createRoom(guiBundle.formatString("new_room_name_prefix", roomInfos.size()),
                guiBundle.formatString("new_room_desc_prefix", roomInfos.size()),
                VotingDomain.class.getName(),
                VotingProtocol.class.getName(),
                MessageManager.class.getName(),
                -1,
                -1,
                stagePath);
    }

    public void sendCreateRoomMessageToWeb(OfferMessage message, String playerId){

    }

    public Set<RoomInfo>  getAllRoomInfos(){
       return server.getRoomController().getAllRoomInfo();
    }

    public String deleteRoom(String roomId){

        RoomInfo roomInfo = server.getRoomController().getRoom(roomId).generateInfo();
        if(roomInfo.getAmountOfPlayers() != 0){
            return "deleteFalse";
        }
        remoteAdmin.deleteRoom(roomId);
        return "deleteTrue";
    }




    public String start_player(){

        return remoteAdmin.launchClient();
    }
}
