package com.swu.agentlab.zsnp.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerSet;
import com.swu.agentlab.zsnp.entity.player.server.MyServer;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.entity.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.management.Query;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/room")
@Component
public class MyRoomWebSocket {

    private static ConfigurableApplicationContext applicationContext;

    private WebVotingArena webVotingArena;


    public static void setApplicationContext(ConfigurableApplicationContext context){
        applicationContext = context;
    }

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 所有的对象
     */
    public static List<MyRoomWebSocket> roomWebSockets = new CopyOnWriteArrayList<MyRoomWebSocket>();

    /**
     * 会话
     */
    private Session session;

//    private List<Session> onlineSession;
    /**
     * 全部在线会话  PS: 基于场景考虑 这里使用线程安全的Map存储会话对象。
     */

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session)
    {
        webVotingArena = applicationContext.getBean(WebVotingArena.class);
        onlineNumber++;
        roomWebSockets.add(this);
        webVotingArena.setMyRoomWebSockets(roomWebSockets);
        this.session = session;
        System.out.println("有新player加入！ 当前创建玩家页面数" + onlineNumber);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;
        roomWebSockets.remove(this);
        webVotingArena.setMyRoomWebSockets(roomWebSockets);
        System.out.println("有创建玩家页面关闭！ 当前创建玩家页面数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param session 会话
     */
    @OnMessage
    public void onMessage(Session session,String jsonStr) {
        QueryStatue queryStatue = JSON.parseObject(jsonStr,QueryStatue.class);
        Set<RoomInfo> roomInfos = new HashSet<>();
        if(queryStatue.getStatue().equals("ALL STATE")){
            sendMessage((RoomMessage.jsonStr(webVotingArena.getServer().getRoomController().getAllRoomInfo(),"UpdateRoom")));
        }else{
            for(RoomInfo roomInfo:webVotingArena.getServer().getRoomController().getAllRoomInfo()){
                if(roomInfo.getStatue().toString().equals(queryStatue.getStatue())){
                    roomInfos.add(roomInfo);
                }
            }
            sendMessage(RoomMessage.jsonStr(roomInfos,"UpdateRoom"));
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(String message)
    {
        System.out.println("session是否关闭");
        System.out.println(session.isOpen());
        if(session.isOpen()) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//

    private synchronized void sendMessageToOther(String msg, String roomId) {
        for (MyRoomWebSocket myRoomWebSocketNew : roomWebSockets) {
            myRoomWebSocketNew.sendMessage(msg);
        }
    }

}
