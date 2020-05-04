package com.swu.agentlab.zsnp.service.player;

import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import com.swu.agentlab.zsnp.entity.communicator.pipeline.Pipe;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.LoginRequest;
import com.swu.agentlab.zsnp.entity.message.body.PlayerLaunch;
import com.swu.agentlab.zsnp.entity.player.PlayerType;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.player.human.RemoteHuman;
import com.swu.agentlab.zsnp.gui.config.GameConfBundle;
import com.swu.agentlab.zsnp.gui.player.Launch;
import com.swu.agentlab.zsnp.gui.player.LaunchForm;
import com.swu.agentlab.zsnp.service.communicator.PipeService;
import com.swu.agentlab.zsnp.service.communicator.SocketService;
import com.swu.agentlab.zsnp.util.Tuples2;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Year;

public class RemotePlayerService implements PlayerService {

    public static RemotePlayer createRemotePlayer(String serverAddr, int port) throws IOException, InterruptedException {
        SocketService socketService = new SocketService();
        MySocket socket = socketService.createMySocket(serverAddr, port);
        RemotePlayer player = new RemotePlayer();
        player.setCommunicator(socket);
        Launch launch = new LaunchForm(player);
        player.setLaunch(launch);
        //属性设置完成之后开始建立消息通道
        player.connect();
        Message message = new Message(null, null, null, new PlayerLaunch(), null);
        player.sendMessage(message);
        return player;
    }

    public static Tuples2<RemotePlayer> createRemotePlayerPair(){
        RemotePlayer player1 = new RemotePlayer();
        RemotePlayer player2 = new RemotePlayer();
        Tuples2<Pipe> pipePair = PipeService.createPipePair();
        player1.setCommunicator(pipePair.get1());
        player2.setCommunicator(pipePair.get2());
        pipePair.get1().setHandler(player1);
        pipePair.get2().setHandler(player2);
        Launch launch = new LaunchForm(player2);
        player2.setLaunch(launch);
        player1.connect();
        player2.connect();
        Tuples2 tuples2 = new Tuples2(player1, player2);
        return  tuples2;
    }

    private RemoteHuman createRemoteHuman(String addr, int port, String name, String description) throws IOException, InterruptedException{
        SocketService socketService = new SocketService();
        MySocket socket = socketService.createMySocket(addr, port);
        RemoteHuman human = new RemoteHuman(name, description, socket);
        human.connect();
        Message message = new Message(null, null,null, new LoginRequest(human.getId(), name, description, PlayerType.REMOTE_HUMAN, null, null), null);
        human.sendMessage(message);
        return human;
    }

    public static void main(String[] args) {
        try {
            GameConfBundle gameConfBundle = GameConfBundle.getInstance("game");
            String addrStr = gameConfBundle.getString("server_addr");
            String portStr = gameConfBundle.getString("server_port");
            String addr = "127.0.0.1";
            int port = 1234;
            if(!StringUtils.isEmpty(addrStr)){
                addr = addrStr;
            }
            if(!StringUtils.isEmpty(portStr)){
                port = Integer.parseInt(portStr);
            }
            RemotePlayerService.createRemotePlayer(addr, port);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
