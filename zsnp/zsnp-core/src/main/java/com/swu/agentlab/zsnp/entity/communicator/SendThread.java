package com.swu.agentlab.zsnp.entity.communicator;

import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.BodyType;
import lombok.Synchronized;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author JJ.Wu
 */
public class SendThread extends Thread{

    private static Logger log = Logger.getLogger(SendThread.class);

    private ObjectOutputStream oos;

    //private Message message;

    private Object lock;

    private LinkedBlockingQueue<Message> messages;

    private IOException exception;

    private Object endLock;

    public SendThread(ObjectOutputStream oos){
        this.oos = oos;
        this.lock= new Object();
        this.endLock = new Object();
        this.messages = new LinkedBlockingQueue<>();
    }

    public synchronized void send(Message message) {
        //this.message = message;
        try {
            messages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //this.unlock();
    }

    public void unlock(){
        synchronized (lock){
            this.lock.notify();
        }
    }

    private void writeMessage() throws IOException {
        synchronized (oos){
            try {
                Message message = messages.take();
                oos.writeObject(message);
                oos.flush();
                oos.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void lock() {
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while(true){
            try{
                //lock();
                writeMessage();
            }catch (IOException e){
                exception = e;
                //e.printStackTrace();
                if(oos != null){
                    try {
                        oos.close();
                    } catch (IOException e1) {
                        //e1.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
