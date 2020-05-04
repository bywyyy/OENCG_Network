package com.swu.agentlab.zsnp.springboot.Myclass;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.PlayerDisconnect;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.player.PlayerSet;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.player.human.RemoteAgent;
import com.swu.agentlab.zsnp.entity.player.human.RemoteHuman;
import com.swu.agentlab.zsnp.entity.player.server.MyServer;
import com.swu.agentlab.zsnp.entity.player.server.Server;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.DomainStreamLoader;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.*;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.springboot.ZsnpApplication;
import com.swu.agentlab.zsnp.springboot.controller.MyRoomWebSocket;
import com.swu.agentlab.zsnp.springboot.controller.MyWebSocket;
import com.swu.agentlab.zsnp.springboot.entity.*;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import javax.annotation.processing.SupportedSourceVersion;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@Data
@Service
public class WebVotingArena {

    private GUIBundle guiBundle;
    private Server server;
    private Map<String,MyWebSocket> webSockets = new HashMap<>();
//            new ConcurrentHashMap<>();
    private List<MyRoomWebSocket> myRoomWebSockets;

    private Map<String,RemoteHuman> remoteHumanMap = new HashMap<>();

    private Map<String, String> nameToId = new HashMap<>();

    private Map<String, String> idToRoleName = new HashMap<>();

    private VotingDomain votingDomain;

    public WebVotingArena(){
        guiBundle =guiBundle = GUIBundle.getInstance("arena");
        this.server = ZsnpApplication.server;
        ZsnpApplication.server.setWebVotingArena(this);
        ZsnpApplication.server.getRemoteAdmin().setWebVotingArena(this);
    }

    public void sendProposal(int reawards,String MyId,String MyName,int MyReward,String Other1Name,
                             int Other1Reward,String Other2Name,int Other2Reward,String Other3Name,int Other3Reward,
                             String Other4Name,int Other4Reward,String roomId) {

        String Other1Id = nameToId.get(Other1Name);
        String Other2Id = nameToId.get(Other2Name);
        String Other3Id = nameToId.get(Other3Name);
        String Other4Id = nameToId.get(Other4Name);
        Proposal proposal = generateProposal(reawards,MyId,MyReward,Other1Id,Other1Reward,Other2Id,Other2Reward,
                Other3Id,Other3Reward,Other4Id,Other4Reward,roomId);
        if (proposal != null) {
            RemoteHuman remoteHuman = remoteHumanMap.get(MyName);
            remoteHuman.sendMessage(new Message(null, null, null, new CounterBody(roomId, null, proposal), null));

//            webSockets.get(MyId).sendMessage(ButtonMessage.jsonStr("FALSE","FALSE","FALSE","FALSE","BUTTONMESSAGE"));
            //            webSockets.get(MyId).sendMessage(Type.jsonStr("SETPROPOSERFALSE"));
//            设置页面为不可提出方案样式,通过前端逻辑控制关闭提出方案按钮
//            setProposer(false);
        }
    }

    public void sendCommunicate(String roomId,String MyName,String MyId,String communicateFree,String communicateType,String emotion){
        Communicate communicate = generateCommunicate(roomId,MyId,communicateFree,communicateType,emotion);
        if(communicateType!=null){
            RemoteHuman remoteHuman = this.remoteHumanMap.get(MyName);
            remoteHuman.sendMessage(new Message(null, null, null, new CommunicateBody(roomId,communicate), null));
        }
    }

    private Communicate generateCommunicate(String roomId,String MyId,String communicateFree,String communicateType,String emotion){
        Communicate communicate = new Communicate();
        communicate.setPlayerInfo(playerSet(roomId).get(MyId).generatePlayerInfo());
        communicate.setCommunicateFree(communicateFree);
        communicate.setCommunicateType(communicateType);
        communicate.setEmotion(emotion);
        return communicate;
    }
    public void setSocketMessageController(String roomId,String playerId,String roleName){
        RemotePlayer remotePlayer = ((MyServer) server).getPlayer2Map().get(playerId);
        
        RemoteHuman remoteRole = null;
        while(remoteRole == null){
            if(remotePlayer.getRemoteHuman() == null)
            {
                remoteRole = remotePlayer.getRemoteAgent();
            }else{
                remoteRole =  remotePlayer.getRemoteHuman();

            }    
        }

        this.remoteHumanMap.put(remoteRole.getName(),remoteRole);
        this.nameToId.put(remoteRole.getName(),remoteRole.getId());
        this.idToRoleName.put(remoteRole.getId(),roleName);

        this.votingDomain = (VotingDomain) remoteRole.getDomain();

        remoteRole.setWebVotingArena(this);
    }

    public void sendServerMessageToWeb(ServerMessage message,String playerId){
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        playerWebSocket.sendMessage(ServerMessage.jsonStr(message.getServerMessage(),message.getTime(),message.getType()));
    }

    public void sendOfferMessageToWeb(OfferMessage message, String playerId){
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        playerWebSocket.sendMessage(OfferMessage.jsonStr(message.getOffer(),message.getTime(),message.getType(),message.getSessionNum()));
    }

    public void sendCommunicateMessageToWeb(CommunicateMessage message,String playerId){
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        playerWebSocket.sendMessage(CommunicateMessage.jsonStr(message.getCommunicateLanguage(),message.getPlayerName(),message.getRoleName(),message.getPlayerNum(),message.getTime(),message.getType(),message.getEmotion()));
    }

    public void sendSessionResultMessageToWeb(OfferMessage message, String playerId){
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        SessionResult sessionResult=(SessionResult)message.getOffer();
        Set<ResultRewardMessage> resultRewardMessages = new HashSet<>();
        for(String name: sessionResult.getResults().keySet()) {
            ResultRewardMessage resultRewardMessage = new ResultRewardMessage();
            resultRewardMessage.setName(name);
            resultRewardMessage.setReward(sessionResult.getResults().get(name));
            resultRewardMessages.add(resultRewardMessage);
        }
        if(sessionResult.isAgree()){
            playerWebSocket.sendMessage(SessionResultMessage.jsonStr(resultRewardMessages,message.getType(),
                    "succeed",votingDomain.getParties().size()));
//            guiBundle.getString("session_result_title")+"("+guiBundle.getString("session_result_title_succeed")+")"
        }else{
            playerWebSocket.sendMessage(SessionResultMessage.jsonStr(resultRewardMessages,message.getType(),
                    "false",votingDomain.getParties().size()));
//            guiBundle.getString("session_result_title")+"("+guiBundle.getString("session_result_title_failed")+")"
        }

        playerWebSocket.sendMessage(OfferMessage.jsonStr(message.getOffer(),message.getTime(),message.getType(),message.getSessionNum()));
    }
    public void sendResponseMessageToWeb(OfferMessage message, String playerId,String emotion) {
        MyWebSocket playerWebSocket = webSockets.get(playerId);
        Response response = (Response)message.getOffer();
        int playerNum = ((PartyInfo)response.getPlayerInfo().getRoleInfo()).getPartyNum();
        if(response.isAgree()){
            playerWebSocket.sendMessage(ResponseMessage.jsonStr(response.getPlayerInfo().getName(),response.getPlayerInfo().getRoleInfo().getRoleName(),
                    guiBundle.getString("response_agree"),message.getType(),"AGREE",emotion,playerNum));

        }else{
            playerWebSocket.sendMessage(ResponseMessage.jsonStr(response.getPlayerInfo().getName(),response.getPlayerInfo().getRoleInfo().getRoleName(),
                    guiBundle.getString("response_refuse"),message.getType(),"REFUSE",emotion,playerNum));
        }
    }


    public void sendSessionEndMessageToWeb(OfferMessage message, String playerId) {


    }

    public void sendProposalMessageToWeb(OfferMessage message,String playerId,String emotion){
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        Set<ProposalMessage> offermessages = new HashSet<>();
        Proposal proposal = (Proposal) message.getOffer();

        System.out.println(proposal.getRoundNum());
        System.out.println(proposal.getSessionRoundNum());
        String proposalPlayerName = null;
        String proposalCompanyName = null;
        int proposalPlayerNum = -1;
        String coalitionText="";
        List<Integer> a = new ArrayList<Integer>();
        for(PlayerInfo playerInfo: proposal.getProposerz().keySet()) {
            //lbl_sender.setText(playerInfo.getName()+"("+playerInfo.getRoleInfo().getRoleName()+")");
            ProposalMessage proposalMessage = new ProposalMessage();
            proposalMessage.setPlayerName(playerInfo.getName());
            proposalMessage.setCompanyName(idToRoleName.get(playerInfo.getId()));
            proposalMessage.setReward(proposal.getProposerz().get(playerInfo));
            proposalMessage.setPlayerNum(((PartyInfo)playerInfo.getRoleInfo()).getPartyNum());
            proposalPlayerName = playerInfo.getName();
            proposalPlayerNum = ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum();
            proposalCompanyName = idToRoleName.get(playerInfo.getId());
            offermessages.add(proposalMessage);
            int partyNum = ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum();
            a.add(partyNum);
        }
        for(PlayerInfo playerInfo: proposal.getAlliesz().keySet()) {
            //lbl_sender.setText(playerInfo.getName()+"("+playerInfo.getRoleInfo().getRoleName()+")");
            ProposalMessage proposalMessage = new ProposalMessage();
            proposalMessage.setPlayerName(playerInfo.getName());
            proposalMessage.setCompanyName(idToRoleName.get(playerInfo.getId()));
            proposalMessage.setReward(proposal.getAlliesz().get(playerInfo).getReward());
            proposalMessage.setPlayerNum(((PartyInfo)playerInfo.getRoleInfo()).getPartyNum());
            offermessages.add(proposalMessage);
//            if(playerId.equals(playerInfo.getId())){
//                playerWebSocket.sendMessage(ButtonMessage.jsonStr("FALSE","TRUE","TRUE","FLASE","BUTTONMESSAGE"));
//            }
            int partyNum = ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum();
            a.add(partyNum);
        }
        Collections.sort(a);
        for(Integer list : a) {
            coalitionText = coalitionText+list;
        }
        System.out.println("Emotion");
        int amountOfPlayer = votingDomain.getParties().size();

        playerWebSocket.sendMessage(ProposalMessages.jsonStr(offermessages,"PROPOSALMESSAGE",proposalPlayerName,
                proposalCompanyName,proposal.getSessionRoundNum(),message.getSessionNum(),proposalPlayerNum,coalitionText,emotion,amountOfPlayer));
    }

    public void sendButtonMessageToWeb(ButtonMessage message, String playerId){
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        System.out.println(idToRoleName.get(playerId));
        playerWebSocket.sendMessage(ButtonMessage.jsonStr(message.getProposal(),message.getAccept(),message.getReject(),message.getSendCommunicate(),message.getType()));
    }
/*    public VotingDomain getVotingDomain(String roomId){
        return (VotingDomain) server.getRoomController().getRoom(roomId).getDomain();
    }*/
    public PlayerSet playerSet(String roomId){
        return server.getRoomController().getRoom(roomId).getPlayers();
    }



    public void acceptProposal(String MyId,String roomId,String MyName){
        MyWebSocket playerWebSocket= webSockets.get(MyId);
        RemoteHuman remoteHuman = remoteHumanMap.get(MyName);
//        playerWebSocket.sendMessage(ButtonMessage.jsonStr("FALSE","FALSE","FALSE","FALSE","BUTTONMESSAGE"));
        remoteHuman.sendMessage(new Message(null, null,null, new CounterBody(roomId, null, new Response(remoteHuman.generatePlayerInfo(), true, null)), null));
    }

    public void rejectProposal(String MyId,String roomId,String MyName){
//          设置同意拒绝按钮不可见
        //        setAlly(false);
        MyWebSocket playerWebSocket= webSockets.get(MyId);
        RemoteHuman remoteHuman = remoteHumanMap.get(MyName);
//        playerWebSocket.sendMessage(ButtonMessage.jsonStr("FALSE","FALSE","FALSE","FALSE","BUTTONMESSAGE"));
        remoteHuman.sendMessage(new Message(null, null, null, new CounterBody(roomId, null, new Response(remoteHuman.generatePlayerInfo(), false, null)), null));
    }




    private Proposal generateProposal(int reawards,String MyId,int MyReward,String Other1Id, int Other1Reward,String Other2Id,
                                      int Other2Reward,String Other3Id,int Other3Reward,String Other4Id,int Other4Reward,String roomId){

        Map<PlayerInfo, Integer> proposerz = new HashMap<>();
        proposerz.put(playerSet(roomId).get(MyId).generatePlayerInfo(), MyReward);

        Proposal proposal = new Proposal();

        Map<PlayerInfo, Toallyz> alliesz = new HashMap<>();
        if(Other1Reward!=0) {
            Toallyz toallyz = new Toallyz();
            toallyz.setReward(Other1Reward);
            alliesz.put(playerSet(roomId).get(Other1Id).generatePlayerInfo(), toallyz);
        }
        if(Other2Reward!=0){
            Toallyz toallyz = new Toallyz();
            toallyz.setReward(Other2Reward);
            alliesz.put(playerSet(roomId).get(Other2Id).generatePlayerInfo(), toallyz);
        }if(Other3Reward!=0){
            Toallyz toallyz = new Toallyz();
            toallyz.setReward(Other3Reward);
            alliesz.put(playerSet(roomId).get(Other3Id).generatePlayerInfo(), toallyz);
        }if(Other4Reward!=0){
            Toallyz toallyz = new Toallyz();
            toallyz.setReward(Other4Reward);
            alliesz.put(playerSet(roomId).get(Other4Id).generatePlayerInfo(), toallyz);

        }

        proposal.setProposerz(proposerz);


        proposal.setAlliesz(alliesz);



        return proposal;
    }

    public void sendAveragePayoff(Map<String, Double> allPayoff, int sessionNum,String playerId,String type) {
        List<AveragePayoff> averagePayoffs = new ArrayList<AveragePayoff>();
        for(String name: allPayoff.keySet()) {
            System.out.println(allPayoff.get(name));
            AveragePayoff averagePayoff = new AveragePayoff();
            averagePayoff.setName(name);
            Double n = allPayoff.get(name)/sessionNum;
            averagePayoff.setPayoff(n);
            averagePayoffs.add(averagePayoff);
        }

        MyWebSocket playerWebSocket= webSockets.get(playerId);
        playerWebSocket.sendMessage(AveragePayoffMessage.jsonStr(averagePayoffs,type));

    }

    public void sendGameEndMessage(Map<PlayerInfo, Double> results,String cont, String type,String playerId) {
        List<ResultPayoff> resultPayoffList = new ArrayList<ResultPayoff>();
        for(PlayerInfo playerInfo:results.keySet()){
            ResultPayoff resultPayoff = new ResultPayoff();
            String name = playerInfo.getName()+"("+playerInfo.getRoleInfo().getRoleName()+")";
            resultPayoff.setName(name);
            resultPayoff.setPayoff(results.get(playerInfo));
            resultPayoffList.add(resultPayoff);
        }
        MyWebSocket playerWebSocket= webSockets.get(playerId);
        playerWebSocket.sendMessage(GameEndMessage.jsonStr(resultPayoffList,cont,type));
    }

    public void sendXMLToWeb(String myId) {
        MyWebSocket playerWebSocket= webSockets.get(myId);

        Set<Coalition> coalitions =  votingDomain.getCoalitions();
        Set<PartyInfo> parties = votingDomain.getParties();
        int majority = votingDomain.getMajority();
        List<CoalitionXML> coalitionXMLS = new ArrayList<CoalitionXML>();
        for(Coalition coalition:coalitions){
            CoalitionXML coalitionXML = new CoalitionXML();
            coalitionXML.setPartyNums(coalition.getPartyNums());
            String corporationsText = "";
            for(int partyNum: coalition.getPartyNums()){
                for(PartyInfo partyInfo:votingDomain.getParties()) {
                    if(partyInfo.getPartyNum() == partyNum)
                    {
                        corporationsText = corporationsText+partyInfo.getRoleName()+",";
                    }
                }
            }
            corporationsText= corporationsText.substring(0,corporationsText.length() - 1);
            coalitionXML.setCorporationsText(corporationsText);
            coalitionXML.setResources(coalition.getResources());
            coalitionXML.setRewards(coalition.getRewards());
            coalitionXMLS.add(coalitionXML);
        }
        playerWebSocket.sendMessage(XMLMessage.jsonStr(coalitionXMLS,parties,majority,"Coalition"));
    }

    public void sendRoomMessageToWeb(Set<RoomInfo> roomInfos,String type) {
        if (myRoomWebSockets != null) {
            for (MyRoomWebSocket myRoomWebSocket : myRoomWebSockets) {
                myRoomWebSocket.sendMessage(RoomMessage.jsonStr(roomInfos, type));
            }
        }
    }


    public void sendGetRoomMessageToWeb(String type) {
        if(myRoomWebSockets!=null)
        {
            for (MyRoomWebSocket myRoomWebSocket : myRoomWebSockets) {
                myRoomWebSocket.sendMessage(RoomMessage.jsonStr(server.getRoomController().getAllRoomInfo(), type));
            }
        }
    }

/**
 * 加载配置文件读取配置文件中的信息并传送到前端
 *
 */
//    public void loadVotingDomain(){
//        InputStream formStream = null;
//        DomainStreamLoader domainStreamLoader = new DomainStreamLoader();
//
//        try {
//            SAXReader reader = new SAXReader();
//            formStream = domainStreamLoader.getForm();
//            Document formDoc = reader.read(new InputStreamReader(formStream));
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }
}
