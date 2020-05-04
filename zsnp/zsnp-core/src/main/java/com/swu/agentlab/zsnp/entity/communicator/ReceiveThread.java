package com.swu.agentlab.zsnp.entity.communicator;

import com.swu.agentlab.zsnp.entity.message.Message;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceiveThread extends Thread {

    private static Logger log = Logger.getLogger(ReceiveThread.class);

    private ObjectInputStream ois;

    //private Message message;

    private IOException ioEx;

    private Receivable receiver;

    private LinkedBlockingQueue<Message> messages;

    private ConnectionResetHandler handler;

    public ReceiveThread(ObjectInputStream ois, Receivable receiver, ConnectionResetHandler handler){
        this.ois = ois;
        this.receiver = receiver;
        this.handler = handler;
        messages = new LinkedBlockingQueue<>();
        new Thread(()->{
            while (true){
                try {
                    Message message = messages.take();
                    receiver.receiveMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void run() {
        super.run();
        while(true){
            try {
                synchronized (ois){
                    //receiver.receiveMessage(message);
                    Message message = (Message) ois.readObject();
                    messages.put(message);
                }
            } catch (IOException e) {
                ioEx = e;
                //e.printStackTrace();
                if(ois!=null){
                    try {
                        ois.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                handler.handleConnectionReset();
                break;
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}