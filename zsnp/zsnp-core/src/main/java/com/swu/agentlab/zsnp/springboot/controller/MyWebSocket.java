package com.swu.agentlab.zsnp.springboot.controller;


import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerSet;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.entity.MainMessage;
import com.swu.agentlab.zsnp.springboot.entity.PlayerInfoMesssage;
import com.swu.agentlab.zsnp.springboot.entity.SessionMap;
import com.swu.agentlab.zsnp.springboot.entity.UpdateMessage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by wzj on 2018/3/14.
 */
@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {
    private WebVotingArena webVotingArena;

    private static ConfigurableApplicationContext applicationContext;

    public static void setApplicationContext(ConfigurableApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 所有的对象
     */
    public static List<MyWebSocket> webSockets = new CopyOnWriteArrayList<MyWebSocket>();

    /**
     * 会话
     */
    private Session session;

    /**
     * 全部在线会话  PS: 基于场景考虑 这里使用线程安全的Map存储会话对象。
     */

    private static HashMap<String, SessionMap> roomOnlineSessions = new HashMap<>();


    private static List<MainMessage> mainMessageList = new ArrayList<MainMessage>();

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineNumber++;
        webSockets.add(this);
        this.session = session;
        System.out.println("有新连接加入！ 当前在线人数" + onlineNumber);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        webSockets.remove(this);
        System.out.println("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param session 会话
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        MainMessage mainMessage = JSON.parseObject(jsonStr, MainMessage.class);
        if (mainMessage.getType().equals("PROPOSAL")) {
            webVotingArena.sendProposal(100, mainMessage.getMyId(), mainMessage.getName1(), mainMessage.getName1Value(),
                    mainMessage.getName2(), mainMessage.getName2Value(), mainMessage.getName3(), mainMessage.getName3Value(),
                    mainMessage.getName4(), mainMessage.getName4Value(), mainMessage.getName5(), mainMessage.getName5Value(), mainMessage.getRoomId());
        } else if (mainMessage.getType().equals("Communicate")) {
//            mainMessageList.add(mainMessage);
//            System.out.println(mainMessageList.size());
//            if(mainMessageList.size() >=3){
//                for(MainMessage mainMessageSmall : mainMessageList){
//                    webVotingArena.sendCommunicate(mainMessageSmall.getRoomId(),mainMessageSmall.getName1(),
//                            mainMessageSmall.getMyId(),mainMessageSmall.getCommunicateFree(),
//                            mainMessageSmall.getCommunicateType(),mainMessage.getEmotion());
//                }
//
//            }
            webVotingArena.sendCommunicate(mainMessage.getRoomId(), mainMessage.getName1(),
                    mainMessage.getMyId(), mainMessage.getCommunicateFree(),
                    mainMessage.getCommunicateType(), mainMessage.getEmotion());


        } else if (mainMessage.getType().equals("UPDATE")) {
            /**
             * 展示收益结构的逻辑
             */
            //更新房间信息
            String roleName;
            webVotingArena = applicationContext.getBean(WebVotingArena.class);
            PlayerSet playerSet = webVotingArena.playerSet(mainMessage.getRoomId());

            if (roomOnlineSessions.get(mainMessage.getRoomId()) == null) {
                SessionMap sessionMap = new SessionMap();
                Map<String, Session> sessionHashMap = new HashMap<>();
                sessionHashMap.put(mainMessage.getMyId(), session);
                sessionMap.setSessionMap(sessionHashMap);
                roomOnlineSessions.put(mainMessage.getRoomId(), sessionMap);
            } else {
                roomOnlineSessions.get(mainMessage.getRoomId()).getSessionMap().put(mainMessage.getMyId(), session);
            }

            webVotingArena.getWebSockets().put(mainMessage.getMyId(), this);


//            while(playerSet.get(mainMessage.getMyId()) == null)
//            {
//                playerSet = webVotingArena.playerSet(mainMessage.getRoomId());
//            };
            playerSet = webVotingArena.playerSet(mainMessage.getRoomId());
            Player me = playerSet.get(mainMessage.getMyId());
            int num = 1;
            PartyInfo myPartyInfo = (PartyInfo) me.getRole().generateRoleInfo();
            roleName = myPartyInfo.getRoleName();
            webVotingArena.setSocketMessageController(mainMessage.getRoomId(), mainMessage.getMyId(), roleName);
            this.sendMessage(PlayerInfoMesssage.jsonStr(me.getName(), me.getDescription(), myPartyInfo.getRoleName(), myPartyInfo.getPartyNum(),
                    myPartyInfo.getResource(), myPartyInfo.getTalent(), myPartyInfo.getTalpublish(), "PLAYER", num, playerSet.size()));
            this.sendMessage(UpdateMessage.jsonStr(webVotingArena.getVotingDomain().getMajority(), "UPDATE"));
            sendMessageToOther(PlayerInfoMesssage.jsonStr(me.getName(), me.getDescription(), myPartyInfo.getRoleName(), myPartyInfo.getPartyNum(),
                    myPartyInfo.getResource(), myPartyInfo.getTalent(), myPartyInfo.getTalpublish(), "PLAYER", playerSet.size(), playerSet.size()), mainMessage.getRoomId());
            num = num + 1;
            for (Player player : playerSet) {
                if (!Objects.equals(player.getId(), mainMessage.getMyId())) {
                    PartyInfo playerPartyInfo = (PartyInfo) player.getRole().generateRoleInfo();
                    this.sendMessage(PlayerInfoMesssage.jsonStr(player.getName(), player.getDescription(), playerPartyInfo.getRoleName(), playerPartyInfo.getPartyNum(),
                            playerPartyInfo.getResource(), playerPartyInfo.getTalent(), playerPartyInfo.getTalpublish(), "PLAYER", num, playerSet.size()));
                    num++;
                }
            }
            webVotingArena.sendXMLToWeb(mainMessage.getMyId());
        } else if (mainMessage.getType().equals("ACCEPT")) {
            webVotingArena.acceptProposal(mainMessage.getMyId(), mainMessage.getRoomId(), mainMessage.getName1());
        } else if (mainMessage.getType().equals("REJECT")) {
            webVotingArena.rejectProposal(mainMessage.getMyId(), mainMessage.getRoomId(), mainMessage.getName1());
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public synchronized void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private synchronized void sendMessageToOther(String msg, String roomId) {
        Map<String, Session> onlineOtherSessions = new ConcurrentHashMap<>();
        for (Iterator it = roomOnlineSessions.get(roomId).getSessionMap().keySet().iterator(); it.hasNext(); ) {
            String key = it.next().toString();
            onlineOtherSessions.put(key, roomOnlineSessions.get(roomId).getSessionMap().get(key));
        }

        onlineOtherSessions.forEach((id, otherSession) -> {

            try {
                if (!otherSession.equals(session)) {
                    otherSession.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }


}