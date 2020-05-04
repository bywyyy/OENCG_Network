package com.swu.agentlab.zsnp.entity.communicator;

import com.swu.agentlab.zsnp.entity.message.Message;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * @author JJ.Wu
 */
public class MySocket extends Communicator implements ConnectionResetHandler{

    private static Logger log = Logger.getLogger(MySocket.class);

    private Socket socket;

    private ReceiveThread receiveThread;

    private SendThread sendThread;

    @Setter
    private DisconnectionHandler handler;

    /**
     * 服务器端初始化MySocket，启动接收和发送现成
     * @param socket
     */
    public MySocket(Socket socket){
        this.socket = socket;
    }

    /**
     * 玩家端初始化MySocket，启动接收、发送线程
     * @param serverAddr
     * @param serverPort
     * @throws IOException
     */
    public MySocket(String serverAddr, int serverPort) throws IOException {
        this.socket = new Socket(serverAddr, serverPort);
    }

    /**
     * 启动发送、接收线程
     */
    @Override
    public void connect() {
        beginCommunication();
    }

    private void beginCommunication(){
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try{
            oos = new ObjectOutputStream(socket.getOutputStream());
            sendThread = new SendThread(oos);
            sendThread.start();
            ois = new ObjectInputStream(socket.getInputStream());
            receiveThread = new ReceiveThread(ois, this, this);
            receiveThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if(socket!=null){
            socket.close();
        }
    }

    @Override
    public void setReceiver(Receivable receiver) {
        this.receiver = receiver;
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
        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 如果hangle == null 表示玩家不处理失去连接的事件
         */
        if(handler != null){
            handler.handleDisconnection();
        }
    }
}
