package com.swu.agentlab.zsnp.entity.player.server;

import com.swu.agentlab.zsnp.controller.room.RoomController;
import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.*;
import com.swu.agentlab.zsnp.entity.player.*;
import com.swu.agentlab.zsnp.entity.player.admin.LaunchHandler;
import com.swu.agentlab.zsnp.entity.player.admin.LocalAdmin;
import com.swu.agentlab.zsnp.entity.player.admin.RemoteAdmin;
import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingProtocol;
import com.swu.agentlab.zsnp.game.coalition.voting.room.MessageManager;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.gui.config.GameConfBundle;
import com.swu.agentlab.zsnp.service.admin.AdminService;
import com.swu.agentlab.zsnp.service.player.RemotePlayerService;
import com.swu.agentlab.zsnp.springboot.Combination.ALLThreeAgents;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.controller.MyRoomWebSocket;
import com.swu.agentlab.zsnp.util.IdUtil;
import com.swu.agentlab.zsnp.util.TimeUtil;
import com.swu.agentlab.zsnp.util.Tuples2;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JJ.Wu
 */

public class MyServer extends Server implements LaunchHandler, PlayerExitHandler {
    /**
     * 父类中有roomcontroller，roomcontroller中包含了room中的各项信息
     */

    private ServerSocket serverSocket;

    private int amountOfRemotePlayers;
    @Getter
    private PlayerSet players;

    private LocalAdmin localAdmin;
    @Getter
    public RemoteAdmin remoteAdmin;
//    private WebCreateRoom webCreateRoom = new WebCreateRoom();
    @Getter
//    private RemotePlayer player2;

    private Map<String,RemotePlayer> player2Map = new HashMap<>();
    @Getter
    private Map<String,RemotePlayer> player1Map = new HashMap<>();
    @Setter
    private WebVotingArena webVotingArena;

    private static Logger log = Logger.getLogger("MyServer");

    private GUIBundle guiBundle;

    @Getter
    private Room agentRoom;
    public MyServer(int port) {
        roomController = new RoomController();
        localAdmin = new LocalAdmin(roomController);
        players = new PlayerSet();
        guiBundle = GUIBundle.getInstance("admin");
        try {
            this.serverSocket = new ServerSocket(port);
            startAdmin(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAdmin(int port){
//        new Thread(()->{
            try {
                remoteAdmin = AdminService.createRemoteAdmin("localhost",port);
                remoteAdmin.setLaunchHandler(this);


              /*  String[] all = new String[]{"AgentProselfA",
                        "AgentProselfAConcede",
                        "AgentProselfB"};

//                ChooseYourAgents agents = null;
//                try{
//                    agents = new ChooseYourAgents(all, 2);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Object[][] all_agent_combination = agents.getResutl();
                Integer[][] agentsPermutation = ALLThreeAgents.all_agent_indexs();



                for(int i = 0; i<agentsPermutation.length;i++){
                    Room room = remoteAdmin.createRoom(guiBundle.formatString("new_room_name_prefix", this.getRoomController().getAllRoomInfo().size()),
                            guiBundle.formatString("new_room_desc_prefix", this.getRoomController().getAllRoomInfo().size()),
                            VotingDomain.class.getName(),
                            VotingProtocol.class.getName(),
                            MessageManager.class.getName(),
                            11,
                            50);
//                    Object[] combination = all_agent_combination[i];
                    Integer[] permutation = agentsPermutation[i];

                    for(int j = 0;j<permutation.length;j++){
                        String playerId = remoteAdmin.launchClient();
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        LoginRequest request;


                        request = generateLoginRequest(String.valueOf(j),all[permutation[j]],agentRoom.getId(),all[permutation[j]] ,"remote_agent");
                        Message message = new Message(null, null, null, request, null);
                        this.getPlayer2Map().get(playerId).receiveMessage(message);
                    }
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }).start();
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


    @Override
    public  void receiveMessage(Message message) {
        //log.info(message);
        message.setTime(TimeUtil.getHms());
        Body cont = message.getBody();
        switch (cont.getBodyType()){
            case COMMUNICATE_BODY:
                new Thread(()->{
                    CommunicateBody communicateBody = (CommunicateBody) cont;
                    roomController.receiveCommunicateBody(communicateBody.getRoomId(), communicateBody);
                }).start();
                break;
            case PLAYER_LAUNCH:

                break;
            case LOGIN_REQUEST:

                /**
                 * 创建玩家的时候，调用的部分
                 */
                /**
                 * 将player添加到room中后在Myserver端就减去这个player
                 */
                LoginRequest loginReq = (LoginRequest) message.getBody();
                String playerId = loginReq.getPlayerId();
                Player player = players.get(playerId);
                player.setName(loginReq.getName());
                player.setType(loginReq.getPlayerType());
                System.out.println(loginReq.getPlayerType());
                player.setPath(loginReq.getAgentPath());
                player.setDescription(loginReq.getDescription());
                String roomId = loginReq.getRoomId();
                String hint = roomController.addPlayer(player, roomId);
                Message msg = new Message(TimeUtil.getHms());
                LoginResponse loginRes = null;
                if("".equals(hint)){
                    players.remove(player);
                    /**
                     * 这个roomController拿到Room信息是最新的room信息，我们前端的Room信息可以从这里拿
                     */
                    RoomInfo info = roomController.getRoom(roomId).generateInfo();
                    if(webVotingArena!=null) {
                        webVotingArena.sendRoomMessageToWeb(roomController.getAllRoomInfo(), "UpdateRoom");
                    }
                    /**
                     * RoomInfo发生了改变，将改变信息传送到前端就行处理,这个players中的信息就是创建页面中的房间信息发生了改变
                     */
                    this.sendMessage(players, new Message(null, null, null, new RoomInfoChange(info), TimeUtil.getHms()));
                    //loginRes = new LoginResponse(true, domain, role, "You are the "+amountOfPlayers+"th player of this room.");
                    //roomController.getRoom(roomId).getMessageManager().notifyLoginMessage();
                }else{
                    /**
                     * 如果信息错误那就返回信息，并且做出相应的反应
                     */
                    loginRes = new LoginResponse(false, null, null, null, hint);
                    msg.setBody(loginRes);
                    player.sendMessage(msg);
                }
                //player.sendMessage(msg);
                //log.info(loginRes);
                break;
            case COUNTER_BODY:
                new Thread(()->{
                    CounterBody counterBody = (CounterBody) cont;
                    roomController.receiveCounterBody(counterBody.getRoomId(), counterBody);
                }).start();
                break;
            case ROOM_CREATE:
                RoomCreate roomCreate = (RoomCreate) cont;
                Room room = localAdmin.createRoom(roomCreate.getName(),
                        roomCreate.getDescription(),
                        roomCreate.getDomainClass(),
                        roomCreate.getProtocolClass(),
                        roomCreate.getMessageManagerClass(),
                        roomCreate.getMaxStage(),
                        roomCreate.getMaxRound(),
                        roomCreate.getStagePath());
                if(roomCreate.getMaxStage()!= -1) {
                    this.agentRoom = room;
                }
//                webVotingArena.sendRoomMessageToWeb(room.generateInfo(),"AddRoom");
                if(room != null){
                    sendMessage(players, new Message(null, null, null, new NewRoom(room.generateInfo()), TimeUtil.getHms()));
                }
                break;
            case DELETE_ROOM_CMD:
                DeleteRoomCmd deleteRoomCmd = (DeleteRoomCmd) cont;
                String roomId1 = deleteRoomCmd.getRoomId();
                roomController.deleteRoom(roomId1);
                message.setTime(TimeUtil.getHms());
                this.sendMessage(players, message);
                break;
            case SELECT_ROOM_CMD:
                SelectRoomCmd selectRoomCmd = (SelectRoomCmd) cont;
                Player player1 = players.get(selectRoomCmd.getPlayerId());
                if(player1 == null){
                }
                Set<RoomInfo> roomInfos = roomController.selectRoomsByNameStatue(selectRoomCmd.getRoomName(), selectRoomCmd.getRoomStatue());
                this.sendMessage(player1, new Message(null, null, null, new RoomsBody(roomInfos), TimeUtil.getHms()));
                break;
            default:

                break;
        }
    }

//    private void sendMessage(WebCreateRoom webCreateRoom, Message message){
//        webCreateRoom.receiveMessage(message);
//    }


    private void sendMessage(PlayerSet players, Message message){
        for(Player item: players){
            item.sendMessage(message);
        }
    }

    private void sendMessage(Player player, Message message){
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(Message message) {
    }

    /**
     * acceptRemotePlayerl开启服务器端后进入死循环等待RemotePlayer的链接
     */
    @Override
    public void acceptRemotePlayer() {
        while(true){
            try {
                synchronized(serverSocket) {
                    Socket socket = serverSocket.accept();
                    MySocket mySocket = new MySocket(socket);
                    String playerId = IdUtil.generate16HexId();
                    String remoteAddr = IdUtil.getSocketIdentifier(socket);
                    RemotePlayer remotePlayer = new RemotePlayer(playerId, mySocket, remoteAddr, this);
                    remotePlayer.setExitHandler(this);
                    remotePlayer.connect();
                    players.add(remotePlayer);
                    /**
                     * 发送ID和房间信息
                     */
                    this.sendLaunchResponse(remotePlayer);
                    /*Set<RoomInfo> infos = roomController.getAllRoomInfo();
                    Message launchResp = new Message(null, null, null, new LaunchResponse(playerId, infos), TimeUtil.getHms());
                    remotePlayer.sendMessage(launchResp);*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptLocalPlayer() {
    }

    /**
     * 这部分是MyServer接收player并且给player赋予id
     * @param player
     */
    private String acceptPlayer(Player player){
        String playerId = IdUtil.generate16HexId();
        player.setId(playerId);
        players.add(player);
        this.sendLaunchResponse(player);
        return  playerId;
    }

    /**
     * 给player发送LaunchResponse的相关信息
     * @param player
     */
    private void sendLaunchResponse(Player player){
        Message message = new Message(null, null, null, new LaunchResponse(player.getId(), roomController.getAllRoomInfo()), TimeUtil.getHms());
        player.sendMessage(message);
    }

    public static void main(String[] args) {
        GameConfBundle gameConfBundle = GameConfBundle.getInstance("game");
        String addrStr = gameConfBundle.getString("server_addr");
        if(!StringUtils.isEmpty(addrStr)){
            if(!"127.0.0.1".equals(addrStr.trim()) && !"localhost".equals(addrStr.trim())){
                RemotePlayerService.main(args);
                return;
            }
        }
        String portStr = gameConfBundle.getString("server_port");
        int port = 1234;
        if(!StringUtils.isEmpty(portStr)){
            port = Integer.parseInt(portStr);
        }
        Server server = new MyServer(port);
        server.start();
    }


    /**
     * 创建一个通道接收player的信息，同时创建player
     */
    @Override
    public String launchClient() {
        Tuples2<RemotePlayer> tuples2 = RemotePlayerService.createRemotePlayerPair();
        tuples2.get1().setReceiver(this);
        tuples2.get1().setExitHandler(this);
        String playerId = acceptPlayer(tuples2.get1());
        player2Map.put(playerId,tuples2.get2());
        player1Map.put(playerId,tuples2.get1());
        return playerId;
    }




    @Override
    public void handleExit(Player player) {
        players.remove(player);
        player = null;
        //log.info(players.size());
    }
}
