package com.swu.agentlab.zsnp.springboot.Myclass;

import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.LoginRequest;
import com.swu.agentlab.zsnp.entity.player.PlayerType;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.player.admin.RemoteAdmin;
import com.swu.agentlab.zsnp.entity.player.human.RemoteAgent;
import com.swu.agentlab.zsnp.entity.player.human.RemoteHuman;
import com.swu.agentlab.zsnp.entity.player.server.MyServer;
import com.swu.agentlab.zsnp.entity.player.server.Server;
import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingProtocol;
import com.swu.agentlab.zsnp.game.coalition.voting.room.MessageManager;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.springboot.Combination.ALLThreeAgents;
import com.swu.agentlab.zsnp.springboot.ZsnpApplication;
import com.swu.agentlab.zsnp.springboot.controller.MyWebSocket;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.websocket.Session;


@Data
@Service
public class Launch {

    private GUIBundle guiBundle;
    private GUIBundle guiBundleAdmin;

    private Server server;
    private RemoteAdmin remoteAdmin;
    public Launch(){
        this.guiBundle = GUIBundle.getInstance("launcher");
        this.server = ZsnpApplication.server;
        this.remoteAdmin = ((MyServer) server).remoteAdmin;
        this.guiBundleAdmin = GUIBundle.getInstance("admin");
    }




    public void btn_ok(String playerId,String name,String desc,String roomId,String agentPath,String playerType){
        LoginRequest request = generateLoginRequest(name,desc,roomId,agentPath,playerType);
        if(request==null){
            return;
        }
        Message message = new Message(null, null, null, request, null);
        ((MyServer) server).getPlayer2Map().get(playerId).receiveMessage(message);
    }

    public void mulitAgent(String agentPath1, String agentPath2, String agentPath3, int maxStage, int maxRound, Session session){

        String[] all = new String[]{agentPath1,agentPath2,agentPath3};

//                ChooseYourAgents agents = null;
//                try{
//                    agents = new ChooseYourAgents(all, 2);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Object[][] all_agent_combination = agents.getResutl();
        Integer[][] agentsPermutation = ALLThreeAgents.all_agent_indexs();

        new Thread(() -> {

            for (int i = 0; i < agentsPermutation.length; i++) {


                Room room = remoteAdmin.createRoom(guiBundleAdmin.formatString("new_room_name_prefix", server.getRoomController().getAllRoomInfo().size()),
                        guiBundleAdmin.formatString("new_room_desc_prefix", server.getRoomController().getAllRoomInfo().size()),
                        VotingDomain.class.getName(),
                        VotingProtocol.class.getName(),
                        MessageManager.class.getName(),
                        maxStage,
                        maxRound,
                        "3Players_weight1-2-2");
//                    Object[] combination = all_agent_combination[i];
                Integer[] permutation = agentsPermutation[i];

                for (int j = 0; j < permutation.length; j++) {
                    String playerId = remoteAdmin.launchClient();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LoginRequest request;


                    request = generateLoginRequest(all[permutation[j]], all[permutation[j]], ((MyServer) server).getAgentRoom().getId(), all[permutation[j]], "remote_agent");
                    Message message = new Message(null, null, null, request, null);


                    ((MyServer) server).getPlayer2Map().get(playerId).receiveMessage(message);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RemotePlayer remotePlayer = ((MyServer) server).getPlayer2Map().get(playerId);
                    RemoteHuman remoteRole = remotePlayer.getRemoteAgent();
                    remoteRole.setSession(session);
                }
            }

        }).start();
    }





    private LoginRequest generateLoginRequest(String playerName, String desc, String roomId,String agentPath, String playerType){


        LoginRequest request;

        if(playerType.equals("remote_human"))
        {
            request = new LoginRequest(
                    "",
                    playerName,
                    desc,
                    PlayerType.REMOTE_HUMAN,
                    null,
                    roomId);
        }
        else{
            request = new LoginRequest(
                    "",
                    playerName,
                    desc,
                    PlayerType.REMOTE_AGENT,
                    agentPath,
                    roomId);

        }
        Class agentClass = null;
        if(agentPath != null) {
            try {
                agentClass = Class.forName(agentPath);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            request.setAgentClass(agentClass);
        }

        return request;
    }
}
