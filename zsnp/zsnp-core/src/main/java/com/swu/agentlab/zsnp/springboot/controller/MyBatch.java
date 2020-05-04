package com.swu.agentlab.zsnp.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.springboot.Myclass.Launch;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.entity.BatchMessage;
import com.swu.agentlab.zsnp.springboot.entity.QueryStatue;
import com.swu.agentlab.zsnp.springboot.entity.RoomMessage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
@ServerEndpoint(value = "/batch")
@Component
public class MyBatch {
    private static ConfigurableApplicationContext applicationContext;


    private Launch launch;
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
    public static List<MyBatch> batches = new CopyOnWriteArrayList<MyBatch>();

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
        this.launch = applicationContext.getBean(Launch.class);
        onlineNumber++;
        batches.add(this);
        this.session = session;
        System.out.println("自动竞赛在线数量" + onlineNumber);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;
        batches.remove(this);
        System.out.println("自动竞赛在线数量" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param session 会话
     */
    @OnMessage
    public void onMessage(Session session,String jsonStr) {
        BatchMessage batchMessage = JSON.parseObject(jsonStr,BatchMessage.class);
        launch.mulitAgent(batchMessage.getAgentPath1(),batchMessage.getAgentPath2(),batchMessage.getAgentPath3(),
                batchMessage.getMaxStage(),batchMessage.getMaxRound(),this.session);
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(String message)
    {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


}
