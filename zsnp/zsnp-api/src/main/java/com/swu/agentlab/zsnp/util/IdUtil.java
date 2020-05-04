package com.swu.agentlab.zsnp.util;


import java.net.Socket;
import java.util.UUID;

public class IdUtil {

    public static String generate32HexId(){
        String str = UUID.randomUUID().toString();
        str = str.replaceAll("-", "");
        str = str.toUpperCase();
        return str;
    }

    public static String generate16HexId(){
        String str = generate32HexId();
        str = str.substring(0, 16);
        return str;
    }

    /**
     * 获取socket的远程地址
     * 这个地址可以唯一标识一个远程连接
     * 用于服务器
     * @param socket
     * @return ip地址:端口号（/127.0.0.1:5348）
     */
    public static String getSocketIdentifier(Socket socket){
        return socket.getRemoteSocketAddress().toString();
    }

    public static void main(String[] args) {
        System.out.println(generate32HexId());
        System.out.println(generate16HexId());
    }
}
