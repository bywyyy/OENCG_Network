package com.swu.agentlab.zsnp.entity.player;

import com.swu.agentlab.zsnp.entity.communicator.Sendable;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Agent {

    private String roomId;

    private Sendable sender;

    private Object lock;

    private LinkedBlockingQueue<Message> messages;

    public Agent(){
        this.lock = new Object();
    }

    public Agent(String roomId, Sendable sender){
        this.lock = new Object();
        init(roomId, sender);
    }

    public final void init(String roomId, Sendable sender){
        this.roomId = roomId;
        this.sender = sender;
        this.messages = new LinkedBlockingQueue<>();
        new Thread(()->{
            while(true){
                try {
                    Message message = messages.take();
                    System.out.println("发送的语言模块的内容"+message.getBody().getBodyType());
                    sender.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public final void notifySender(){
        synchronized (lock){
            lock.notify();
        }
    }

    public final void sendOffer(){
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //sleep();

            }
        }).start();*/
        Offer offer = generateOffer();

        // 这部分附加如语言信息的message 发送请求，组装communicate部分的message
        Communicate communicate = generateCommunicate();



        Message communicateMessage = new Message(null, null, null, new CommunicateBody(roomId,communicate), null);
        Message message = new Message(null, null, null, new CounterBody(roomId, null, offer), null);

        try {
            if(offer instanceof Response){
                messages.put(message);
            }else{
                messages.put(communicateMessage);
                messages.put(message);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void receiveOffer(Offer offer);

    protected abstract Offer generateOffer();

    protected abstract Communicate generateCommunicate();

    private void sleep(){
        //double sec = new Random().nextDouble();
        try {
            Thread.sleep((int)(50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
