package com.swu.agentlab.zsnp.entity.communicator.pipeline;

import com.swu.agentlab.zsnp.entity.communicator.*;
import com.swu.agentlab.zsnp.entity.message.Message;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.*;

public class Pipe extends Communicator implements ConnectionResetHandler {

    private final Logger log = Logger.getLogger(Pipe.class);

    @Getter
    private PipedInputStream pis;

    @Getter
    private PipedOutputStream pos;

    private ReceiveThread receiveThread;

    private SendThread sendThread;

    @Setter
    private DisconnectionHandler handler;

    public Pipe(){
        pis = new PipedInputStream();
        pos = new PipedOutputStream();
    }

    @Override
    public void connect() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(pos);
            sendThread = new SendThread(oos);
            sendThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(pis);
                receiveThread = new ReceiveThread(ois, this, this);
                receiveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect(PipedOutputStream src, PipedInputStream snk){
        try {
            //pis.connect(src);
            pos.connect(snk);
            src.connect(this.pis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(Message message) {
        receiver.receiveMessage(message);
    }

    @Override
    public void sendMessage(Message message) {
        sendThread.send(message);
    }

    @Override
    public void handleConnectionReset() {
        sendThread.unlock();
        try {
            if(pos!=null){
                pos.close();
            }
            if(pis != null){
                pis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(handler != null){
            handler.handleDisconnection();
        }
    }
}
